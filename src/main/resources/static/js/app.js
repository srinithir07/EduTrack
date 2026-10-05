/* ==========================================================================
   EduTrack - Academic Performance & CGPA Management System Application JS
   Communicates directly with Spring Boot REST Controllers (/api/*)
   ========================================================================== */

const API_BASE = '/api';

// Cache store for dropdowns and dashboard calculations
let studentsCache = [];
let subjectsCache = [];
let semestersCache = [];
let performanceCache = [];

document.addEventListener('DOMContentLoaded', () => {
    setupNavigation();
    loadDashboardData();
});

// Navigation View Switcher
function setupNavigation() {
    const navItems = document.querySelectorAll('.nav-item');
    navItems.forEach(item => {
        item.addEventListener('click', () => {
            const targetView = item.getAttribute('data-view');
            switchView(targetView);
        });
    });
}

function switchView(viewName) {
    document.querySelectorAll('.nav-item').forEach(el => el.classList.remove('active'));
    document.querySelectorAll('.view-section').forEach(el => el.classList.remove('active'));

    const navItem = document.querySelector(`.nav-item[data-view="${viewName}"]`);
    const viewSec = document.getElementById(`view-${viewName}`);

    if (navItem) navItem.classList.add('active');
    if (viewSec) viewSec.classList.add('active');

    // Update Header Text
    const titleMap = {
        dashboard: { title: 'Dashboard', sub: 'Overview of institutional academic performance and records' },
        students: { title: 'Student Directory', sub: 'Manage enrolled student records and details' },
        subjects: { title: 'Subject Catalogue', sub: 'Manage academic subjects and credit weights' },
        semesters: { title: 'Semester Records', sub: 'Manage academic terms and calendar years' },
        performance: { title: 'Marks & Performance Entry', sub: 'Record subject marks; Backend automatically computes Grades & Points' },
        summary: { title: 'Academic Reports & Transcripts', sub: 'Calculate semester-wise GPA, overall CGPA, and Academic Standing' }
    };

    if (titleMap[viewName]) {
        document.getElementById('pageTitle').textContent = titleMap[viewName].title;
        document.getElementById('pageSubTitle').textContent = titleMap[viewName].sub;
    }

    // Trigger data fetch for active view
    if (viewName === 'dashboard') loadDashboardData();
    if (viewName === 'students') loadStudentsData();
    if (viewName === 'subjects') loadSubjectsData();
    if (viewName === 'semesters') loadSemestersData();
    if (viewName === 'performance') loadPerformanceData();
    if (viewName === 'summary') populateSummaryStudentDropdown();
}

/* ==========================================================================
   1. DASHBOARD LOGIC
   ========================================================================== */
async function loadDashboardData() {
    try {
        const [students, subjects, semesters, performances] = await Promise.all([
            fetch(`${API_BASE}/students`).then(res => res.json()),
            fetch(`${API_BASE}/subjects`).then(res => res.json()),
            fetch(`${API_BASE}/semesters`).then(res => res.json()),
            fetch(`${API_BASE}/performance`).then(res => res.json())
        ]);

        studentsCache = students;
        subjectsCache = subjects;
        semestersCache = semesters;
        performanceCache = performances;

        document.getElementById('dashTotalStudents').textContent = students.length;
        document.getElementById('dashTotalSubjects').textContent = subjects.length;
        document.getElementById('dashTotalSemesters').textContent = semesters.length;

        // Calculate institutional average CGPA across all students with records
        if (students.length > 0 && performances.length > 0) {
            const studentSummaries = await Promise.all(
                students.map(s => fetch(`${API_BASE}/academic/students/${s.id}/summary`).then(r => r.json()))
            );
            const validSummaries = studentSummaries.filter(s => s.totalCredits > 0);
            if (validSummaries.length > 0) {
                const avgCgpa = validSummaries.reduce((sum, s) => sum + s.cgpa, 0) / validSummaries.length;
                document.getElementById('dashAvgCgpa').textContent = avgCgpa.toFixed(2);
            } else {
                document.getElementById('dashAvgCgpa').textContent = '0.00';
            }
        } else {
            document.getElementById('dashAvgCgpa').textContent = '0.00';
        }

        renderDashboardRecentTable(performances.slice(-5).reverse());
    } catch (err) {
        showToast('Error loading dashboard metrics', 'error');
    }
}

function renderDashboardRecentTable(records) {
    const tbody = document.getElementById('dashRecentPerformanceBody');
    if (!records || records.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" class="empty-state">No performance records recorded yet.</td></tr>`;
        return;
    }

    tbody.innerHTML = records.map(p => `
        <tr>
            <td><strong>${escapeHtml(p.studentRegisterNumber)}</strong></td>
            <td>${escapeHtml(p.studentName)}</td>
            <td>${escapeHtml(p.subjectCode)} - ${escapeHtml(p.subjectName)}</td>
            <td>Sem ${p.semesterNumber} (${escapeHtml(p.academicYear)})</td>
            <td><strong>${p.marks}</strong></td>
            <td><span class="badge ${getGradeBadgeClass(p.grade)}">${p.grade}</span></td>
            <td><strong>${p.gradePoint}</strong></td>
        </tr>
    `).join('');
}

/* ==========================================================================
   2. STUDENT CRUD LOGIC
   ========================================================================== */
async function loadStudentsData() {
    try {
        const res = await fetch(`${API_BASE}/students`);
        studentsCache = await res.json();
        renderStudentsTable(studentsCache);
    } catch (err) {
        showToast('Failed to load students list', 'error');
    }
}

function renderStudentsTable(students) {
    const tbody = document.getElementById('studentsTableBody');
    if (!students || students.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6" class="empty-state">No students found. Click "+ Add New Student" to create one.</td></tr>`;
        return;
    }

    tbody.innerHTML = students.map(s => `
        <tr>
            <td><strong>${escapeHtml(s.registerNumber)}</strong></td>
            <td>${escapeHtml(s.name)}</td>
            <td>${escapeHtml(s.email)}</td>
            <td>${escapeHtml(s.department)}</td>
            <td>${escapeHtml(s.batch)}</td>
            <td>
                <button class="btn btn-secondary btn-sm" onclick="viewStudentSummary(${s.id})">Summary</button>
                <button class="btn btn-secondary btn-sm" onclick="editStudent(${s.id})">Edit</button>
                <button class="btn btn-danger btn-sm" onclick="deleteStudent(${s.id})">Delete</button>
            </td>
        </tr>
    `).join('');
}

function openStudentModal(student = null) {
    document.getElementById('studentForm').reset();
    document.getElementById('studentId').value = '';
    if (student) {
        document.getElementById('studentModalTitle').textContent = 'Edit Student';
        document.getElementById('studentId').value = student.id;
        document.getElementById('studentRegisterNumber').value = student.registerNumber;
        document.getElementById('studentName').value = student.name;
        document.getElementById('studentEmail').value = student.email;
        document.getElementById('studentDepartment').value = student.department;
        document.getElementById('studentBatch').value = student.batch;
    } else {
        document.getElementById('studentModalTitle').textContent = 'Add Student';
    }
    document.getElementById('studentModal').classList.add('active');
}

function closeStudentModal() {
    document.getElementById('studentModal').classList.remove('active');
}

function editStudent(id) {
    const student = studentsCache.find(s => s.id === id);
    if (student) openStudentModal(student);
}

async function handleStudentSubmit(e) {
    e.preventDefault();
    const id = document.getElementById('studentId').value;
    const body = {
        registerNumber: document.getElementById('studentRegisterNumber').value.trim(),
        name: document.getElementById('studentName').value.trim(),
        email: document.getElementById('studentEmail').value.trim(),
        department: document.getElementById('studentDepartment').value.trim(),
        batch: document.getElementById('studentBatch').value.trim()
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_BASE}/students/${id}` : `${API_BASE}/students`;

    try {
        const res = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });

        if (res.ok) {
            showToast(`Student ${id ? 'updated' : 'created'} successfully!`, 'success');
            closeStudentModal();
            loadStudentsData();
        } else {
            const err = await res.json();
            showToast(err.message || (err.details ? err.details.join(', ') : 'Operation failed'), 'error');
        }
    } catch (err) {
        showToast('Network error while saving student', 'error');
    }
}

async function deleteStudent(id) {
    if (!confirm('Are you sure you want to delete this student? All performance records will be deleted.')) return;
    try {
        const res = await fetch(`${API_BASE}/students/${id}`, { method: 'DELETE' });
        if (res.ok) {
            showToast('Student deleted successfully', 'success');
            loadStudentsData();
        } else {
            showToast('Failed to delete student', 'error');
        }
    } catch (err) {
        showToast('Error deleting student', 'error');
    }
}

function viewStudentSummary(studentId) {
    switchView('summary');
    document.getElementById('summaryStudentSelect').value = studentId;
    loadStudentAcademicSummary(studentId);
}

/* ==========================================================================
   3. SUBJECT CRUD LOGIC
   ========================================================================== */
async function loadSubjectsData() {
    try {
        const res = await fetch(`${API_BASE}/subjects`);
        subjectsCache = await res.json();
        renderSubjectsTable(subjectsCache);
    } catch (err) {
        showToast('Failed to load subjects', 'error');
    }
}

function renderSubjectsTable(subjects) {
    const tbody = document.getElementById('subjectsTableBody');
    if (!subjects || subjects.length === 0) {
        tbody.innerHTML = `<tr><td colspan="4" class="empty-state">No subjects found. Click "+ Add New Subject" to create one.</td></tr>`;
        return;
    }

    tbody.innerHTML = subjects.map(s => `
        <tr>
            <td><strong>${escapeHtml(s.subjectCode)}</strong></td>
            <td>${escapeHtml(s.subjectName)}</td>
            <td><strong>${s.credits} Credits</strong></td>
            <td>
                <button class="btn btn-secondary btn-sm" onclick="editSubject(${s.id})">Edit</button>
                <button class="btn btn-danger btn-sm" onclick="deleteSubject(${s.id})">Delete</button>
            </td>
        </tr>
    `).join('');
}

function openSubjectModal(subject = null) {
    document.getElementById('subjectForm').reset();
    document.getElementById('subjectId').value = '';
    if (subject) {
        document.getElementById('subjectModalTitle').textContent = 'Edit Subject';
        document.getElementById('subjectId').value = subject.id;
        document.getElementById('subjectCode').value = subject.subjectCode;
        document.getElementById('subjectName').value = subject.subjectName;
        document.getElementById('subjectCredits').value = subject.credits;
    } else {
        document.getElementById('subjectModalTitle').textContent = 'Add Subject';
    }
    document.getElementById('subjectModal').classList.add('active');
}

function closeSubjectModal() {
    document.getElementById('subjectModal').classList.remove('active');
}

function editSubject(id) {
    const subject = subjectsCache.find(s => s.id === id);
    if (subject) openSubjectModal(subject);
}

async function handleSubjectSubmit(e) {
    e.preventDefault();
    const id = document.getElementById('subjectId').value;
    const body = {
        subjectCode: document.getElementById('subjectCode').value.trim(),
        subjectName: document.getElementById('subjectName').value.trim(),
        credits: parseInt(document.getElementById('subjectCredits').value)
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_BASE}/subjects/${id}` : `${API_BASE}/subjects`;

    try {
        const res = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });

        if (res.ok) {
            showToast(`Subject ${id ? 'updated' : 'created'} successfully!`, 'success');
            closeSubjectModal();
            loadSubjectsData();
        } else {
            const err = await res.json();
            showToast(err.message || 'Subject save failed', 'error');
        }
    } catch (err) {
        showToast('Network error while saving subject', 'error');
    }
}

async function deleteSubject(id) {
    if (!confirm('Are you sure you want to delete this subject?')) return;
    try {
        const res = await fetch(`${API_BASE}/subjects/${id}`, { method: 'DELETE' });
        if (res.ok) {
            showToast('Subject deleted', 'success');
            loadSubjectsData();
        } else {
            showToast('Failed to delete subject', 'error');
        }
    } catch (err) {
        showToast('Error deleting subject', 'error');
    }
}

/* ==========================================================================
   4. SEMESTER CRUD LOGIC
   ========================================================================== */
async function loadSemestersData() {
    try {
        const res = await fetch(`${API_BASE}/semesters`);
        semestersCache = await res.json();
        renderSemestersTable(semestersCache);
    } catch (err) {
        showToast('Failed to load semesters', 'error');
    }
}

function renderSemestersTable(semesters) {
    const tbody = document.getElementById('semestersTableBody');
    if (!semesters || semesters.length === 0) {
        tbody.innerHTML = `<tr><td colspan="3" class="empty-state">No semesters found. Click "+ Add New Semester" to create one.</td></tr>`;
        return;
    }

    tbody.innerHTML = semesters.map(s => `
        <tr>
            <td><strong>Semester ${s.semesterNumber}</strong></td>
            <td>${escapeHtml(s.academicYear)}</td>
            <td>
                <button class="btn btn-secondary btn-sm" onclick="editSemester(${s.id})">Edit</button>
                <button class="btn btn-danger btn-sm" onclick="deleteSemester(${s.id})">Delete</button>
            </td>
        </tr>
    `).join('');
}

function openSemesterModal(semester = null) {
    document.getElementById('semesterForm').reset();
    document.getElementById('semesterId').value = '';
    if (semester) {
        document.getElementById('semesterModalTitle').textContent = 'Edit Semester';
        document.getElementById('semesterId').value = semester.id;
        document.getElementById('semesterNumber').value = semester.semesterNumber;
        document.getElementById('semesterAcademicYear').value = semester.academicYear;
    } else {
        document.getElementById('semesterModalTitle').textContent = 'Add Semester';
    }
    document.getElementById('semesterModal').classList.add('active');
}

function closeSemesterModal() {
    document.getElementById('semesterModal').classList.remove('active');
}

function editSemester(id) {
    const sem = semestersCache.find(s => s.id === id);
    if (sem) openSemesterModal(sem);
}

async function handleSemesterSubmit(e) {
    e.preventDefault();
    const id = document.getElementById('semesterId').value;
    const body = {
        semesterNumber: parseInt(document.getElementById('semesterNumber').value),
        academicYear: document.getElementById('semesterAcademicYear').value.trim()
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_BASE}/semesters/${id}` : `${API_BASE}/semesters`;

    try {
        const res = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });

        if (res.ok) {
            showToast(`Semester ${id ? 'updated' : 'created'} successfully!`, 'success');
            closeSemesterModal();
            loadSemestersData();
        } else {
            const err = await res.json();
            showToast(err.message || 'Semester save failed', 'error');
        }
    } catch (err) {
        showToast('Network error while saving semester', 'error');
    }
}

async function deleteSemester(id) {
    if (!confirm('Are you sure you want to delete this semester record?')) return;
    try {
        const res = await fetch(`${API_BASE}/semesters/${id}`, { method: 'DELETE' });
        if (res.ok) {
            showToast('Semester deleted', 'success');
            loadSemestersData();
        } else {
            showToast('Failed to delete semester', 'error');
        }
    } catch (err) {
        showToast('Error deleting semester', 'error');
    }
}

/* ==========================================================================
   5. PERFORMANCE & MARKS ENTRY LOGIC
   ========================================================================== */
async function loadPerformanceData() {
    try {
        const [students, subjects, semesters, performances] = await Promise.all([
            fetch(`${API_BASE}/students`).then(r => r.json()),
            fetch(`${API_BASE}/subjects`).then(r => r.json()),
            fetch(`${API_BASE}/semesters`).then(r => r.json()),
            fetch(`${API_BASE}/performance`).then(r => r.json())
        ]);

        studentsCache = students;
        subjectsCache = subjects;
        semestersCache = semesters;
        performanceCache = performances;

        populateDropdown('perfStudentSelect', students, s => `${s.registerNumber} - ${s.name}`);
        populateDropdown('perfSubjectSelect', subjects, sub => `${sub.subjectCode} - ${sub.subjectName} (${sub.credits} Credits)`);
        populateDropdown('perfSemesterSelect', semesters, sem => `Semester ${sem.semesterNumber} (${sem.academicYear})`);

        renderPerformanceTable(performances);
    } catch (err) {
        showToast('Failed to load performance entry data', 'error');
    }
}

function populateDropdown(elementId, items, labelFn) {
    const select = document.getElementById(elementId);
    select.innerHTML = '<option value="">-- Choose Option --</option>' +
        items.map(item => `<option value="${item.id}">${escapeHtml(labelFn(item))}</option>`).join('');
}

function previewGradeCalculation() {
    const marksVal = parseFloat(document.getElementById('perfMarks').value);
    const box = document.getElementById('gradePreviewBox');
    if (isNaN(marksVal) || marksVal < 0 || marksVal > 100) {
        box.style.display = 'none';
        return;
    }

    let grade = 'F', point = 0;
    if (marksVal >= 90) { grade = 'O'; point = 10; }
    else if (marksVal >= 80) { grade = 'A+'; point = 9; }
    else if (marksVal >= 70) { grade = 'A'; point = 8; }
    else if (marksVal >= 60) { grade = 'B+'; point = 7; }
    else if (marksVal >= 50) { grade = 'B'; point = 6; }
    else if (marksVal >= 40) { grade = 'C'; point = 5; }

    document.getElementById('previewGradeCode').textContent = grade;
    document.getElementById('previewGradePoint').textContent = point;
    box.style.display = 'block';
}

async function handlePerformanceSubmit(e) {
    e.preventDefault();
    const body = {
        studentId: parseInt(document.getElementById('perfStudentSelect').value),
        subjectId: parseInt(document.getElementById('perfSubjectSelect').value),
        semesterId: parseInt(document.getElementById('perfSemesterSelect').value),
        marks: parseFloat(document.getElementById('perfMarks').value)
    };

    try {
        const res = await fetch(`${API_BASE}/performance`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });

        if (res.ok) {
            showToast('Marks recorded and backend grade calculated!', 'success');
            document.getElementById('performanceForm').reset();
            document.getElementById('gradePreviewBox').style.display = 'none';
            loadPerformanceData();
        } else {
            const err = await res.json();
            showToast(err.message || 'Failed to save performance record', 'error');
        }
    } catch (err) {
        showToast('Network error while saving performance', 'error');
    }
}

function renderPerformanceTable(records) {
    const tbody = document.getElementById('performanceTableBody');
    if (!records || records.length === 0) {
        tbody.innerHTML = `<tr><td colspan="9" class="empty-state">No performance records created yet. Use the form above to enter marks.</td></tr>`;
        return;
    }

    tbody.innerHTML = records.map(p => `
        <tr>
            <td><strong>${escapeHtml(p.studentRegisterNumber)}</strong></td>
            <td>${escapeHtml(p.studentName)}</td>
            <td>${escapeHtml(p.subjectCode)} - ${escapeHtml(p.subjectName)}</td>
            <td>${p.subjectCredits} Cr</td>
            <td>Sem ${p.semesterNumber} (${escapeHtml(p.academicYear)})</td>
            <td><strong>${p.marks}</strong></td>
            <td><span class="badge ${getGradeBadgeClass(p.grade)}">${p.grade}</span></td>
            <td><strong>${p.gradePoint}</strong></td>
            <td>
                <button class="btn btn-danger btn-sm" onclick="deletePerformance(${p.id})">Delete</button>
            </td>
        </tr>
    `).join('');
}

async function deletePerformance(id) {
    if (!confirm('Are you sure you want to delete this performance record?')) return;
    try {
        const res = await fetch(`${API_BASE}/performance/${id}`, { method: 'DELETE' });
        if (res.ok) {
            showToast('Performance record deleted', 'success');
            loadPerformanceData();
        } else {
            showToast('Failed to delete performance record', 'error');
        }
    } catch (err) {
        showToast('Error deleting performance record', 'error');
    }
}

/* ==========================================================================
   6. ACADEMIC SUMMARY & REPORT LOGIC
   ========================================================================== */
async function populateSummaryStudentDropdown() {
    try {
        const res = await fetch(`${API_BASE}/students`);
        const students = await res.json();
        populateDropdown('summaryStudentSelect', students, s => `${s.registerNumber} - ${s.name} (${s.department})`);
    } catch (err) {
        showToast('Failed to load student list for summary', 'error');
    }
}

async function loadStudentAcademicSummary(studentId) {
    const container = document.getElementById('summaryReportContainer');
    const placeholder = document.getElementById('summarySelectPlaceholder');

    if (!studentId) {
        container.style.display = 'none';
        placeholder.style.display = 'block';
        return;
    }

    try {
        const res = await fetch(`${API_BASE}/academic/students/${studentId}/summary`);
        if (!res.ok) throw new Error('Summary fetch failed');
        const summary = await res.json();

        placeholder.style.display = 'none';
        container.style.display = 'block';

        // Set Banner Info
        document.getElementById('sumStudentName').textContent = summary.student.name;
        document.getElementById('sumStudentMeta').textContent = 
            `Reg: ${summary.student.registerNumber} | Dept: ${summary.student.department} | Batch: ${summary.student.batch}`;

        document.getElementById('sumTotalCredits').textContent = summary.totalCredits;
        document.getElementById('sumCgpa').textContent = summary.cgpa.toFixed(2);

        const statusBadge = document.getElementById('sumAcademicStatusBadge');
        statusBadge.textContent = summary.academicStatus;
        statusBadge.className = `badge ${getStatusBadgeClass(summary.academicStatus)}`;

        // Render Semester Breakdowns
        renderSemesterBreakdown(summary.semesterSummaries);
    } catch (err) {
        showToast('Failed to load academic summary for student', 'error');
    }
}

function renderSemesterBreakdown(semesterSummaries) {
    const listContainer = document.getElementById('semesterBreakdownList');
    if (!semesterSummaries || semesterSummaries.length === 0) {
        listContainer.innerHTML = `<div class="card empty-state">No semester records found for this student.</div>`;
        return;
    }

    listContainer.innerHTML = semesterSummaries.map(sem => `
        <div class="card" style="margin-bottom: 24px;">
            <div style="display:flex; align-items:center; justify-content:space-between; margin-bottom:16px;">
                <div>
                    <h3 style="font-size:16px; font-weight:700;">Semester ${sem.semesterNumber}</h3>
                    <p style="font-size:12px; color:var(--text-muted);">Academic Year: ${escapeHtml(sem.academicYear)} | Total Semester Credits: ${sem.totalCredits}</p>
                </div>
                <div style="text-align:right;">
                    <div style="font-size:12px; color:var(--text-muted); text-transform:uppercase;">Semester GPA</div>
                    <div style="font-size:22px; font-weight:700; color:var(--emerald-600);">${sem.gpa.toFixed(2)}</div>
                </div>
            </div>

            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Subject Code</th>
                            <th>Subject Name</th>
                            <th>Credits</th>
                            <th>Marks</th>
                            <th>Grade</th>
                            <th>Grade Point</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${sem.performances.map(p => `
                            <tr>
                                <td><strong>${escapeHtml(p.subjectCode)}</strong></td>
                                <td>${escapeHtml(p.subjectName)}</td>
                                <td>${p.subjectCredits}</td>
                                <td>${p.marks}</td>
                                <td><span class="badge ${getGradeBadgeClass(p.grade)}">${p.grade}</span></td>
                                <td><strong>${p.gradePoint}</strong></td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            </div>
        </div>
    `).join('');
}

/* Helper Badge Mapping */
function getGradeBadgeClass(grade) {
    switch (grade) {
        case 'O': return 'badge-o';
        case 'A+': return 'badge-a-plus';
        case 'A': return 'badge-a';
        case 'B+': return 'badge-b-plus';
        case 'B': return 'badge-b';
        case 'C': return 'badge-c';
        case 'F': return 'badge-f';
        default: return 'badge-a';
    }
}

function getStatusBadgeClass(status) {
    switch (status) {
        case 'Excellent': return 'badge-status-excellent';
        case 'Good': return 'badge-status-good';
        case 'Satisfactory': return 'badge-status-satisfactory';
        case 'Needs Improvement': return 'badge-status-needs-improvement';
        default: return 'badge-status-good';
    }
}

// Utility Toast Notifications
function showToast(message, type = 'success') {
    const container = document.getElementById('toastContainer');
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.textContent = message;
    container.appendChild(toast);

    setTimeout(() => {
        toast.remove();
    }, 4000);
}

function escapeHtml(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}
