taskList = {'tasks' : []};


function FavoriteDel(checkbox) {
    var span = checkbox.nextElementSibling;
    var fullList = checkbox.parentElement.parentElement.parentElement;
    var id = fullList.querySelector('input[type="hidden"]').value;

    if (checkbox.checked) {
        span.classList.add('line-through');
        fullList.classList.remove('bg-gray-100');
        fullList.classList.add('bg-success');

        for (var i = 0; i < taskList.tasks.length; i++) {
            if (taskList.tasks[i] === id) {
                return;
            }
        }
        taskList.tasks.push(id);


    } else {
        span.classList.remove('line-through');
        fullList.classList.add('bg-gray-100');
        fullList.classList.remove('bg-success');

        for (var i = 0; i < taskList.tasks.length; i++) {
            if (taskList.tasks[i] === id) {
                taskList.tasks.splice(i, 1);
                return;
            }
        }
    }
    console.log(taskList);
}