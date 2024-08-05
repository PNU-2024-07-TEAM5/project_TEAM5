// src/main/resources/static/js/todo.js

function addTask() {
    const taskInput = document.getElementById('taskInput');
    const taskList = document.getElementById('taskList');
    if (taskInput.value.trim() !== '') {
        const li = document.createElement('li');
        li.className = 'flex flex-col justify-between items-start p-2 bg-gray-100 rounded mb-2';
        li.innerHTML = `
            <div class="flex justify-between items-center w-full">
                <input type="checkbox" onclick="toggleTask(this)" class="mr-2">
                <span>${taskInput.value}</span>
                <button onclick="toggleDescription(this)" class="ml-auto bg-gray-300 px-2 py-1 rounded">Toggle</button>
            </div>
            <div class="description mt-2">This is the description for ${taskInput.value}.</div>
        `;
        taskList.appendChild(li);
        taskInput.value = '';
    }
}

function toggleTask(checkbox) {
    const span = checkbox.nextElementSibling;
    if (checkbox.checked) {
        span.classList.add('line-through');
    } else {
        span.classList.remove('line-through');
    }
}

function refreshTasks() {
    const taskList = document.getElementById('taskList');
    taskList.innerHTML = '';
}

function toggleDescription(button) {
    const description = button.parentElement.nextElementSibling;
    if (description.style.display === 'none') {
        description.style.display = 'block';
    } else {
        description.style.display = 'none';
    }
}