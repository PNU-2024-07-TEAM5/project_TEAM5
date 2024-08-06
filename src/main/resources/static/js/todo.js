function toggleTask(checkbox) {
    var span = checkbox.nextElementSibling;
    var fullList = checkbox.parentElement.parentElement;
    console.log(fullList);
    if (checkbox.checked) {
        span.classList.add('line-through');
        fullList.classList.remove('bg-gray-100');
        fullList.classList.add('bg-primary');
    } else {
        span.classList.remove('line-through');
        fullList.classList.add('bg-gray-100');
        fullList.classList.remove('bg-primary');
    }
}

function addTask() {
    var inputForm = document.getElementById('inputForm');
    var task = document.getElementById('taskInput');

    if (task.value.trim() === '') {
        alert('목표를 입력해주세요.');
        task.value = '';
        return;
    } else if (task.value.length > 50) {
        alert('목표는 50자 이내로 입력해주세요.');
        task.value = '';
        return;
    } else if (task.value.length < 5) {
        alert('목표는 5자 이상으로 입력해주세요.');
        task.value = '';
        return;
    } else {
        inputForm.submit();
    }
}