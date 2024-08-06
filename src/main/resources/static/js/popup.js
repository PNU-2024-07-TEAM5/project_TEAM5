function showDivideForm() {
    const divideForm = document.getElementById('divideForm');
    divideForm.classList.remove('hidden');
    divideForm.classList.remo('hidden');
}

function hideDivideForm() {
    const divideForm = document.getElementById('divideForm');
    document.getElementById('divideForm').classList.add('hidden');
    document.getElementById('divideForm').classList.remove('slide-up');
}

function showLoading() {
    document.getElementById('loadingForm').classList.remove('hidden');
}

function hideLoading() {
    document.getElementById('loadingForm').classList.add('hidden');
}

function showSelectForm() {
    const form = document.getElementById('selectForm');
    form.classList.remove('hidden');
    form.classList.remove('opacity-0');
    form.classList.add('opacity-100');
}

function hideSelectForm() {
    const form = document.getElementById('selectForm');
    form.classList.remove('opacity-100');
    form.classList.add('opacity-0',);
    setTimeout(() => form.classList.add('hidden'), 500); // Wait for the fade-out animation to complete
}



function confirmYes() {
    hideSelectForm();
    showLoading();
}