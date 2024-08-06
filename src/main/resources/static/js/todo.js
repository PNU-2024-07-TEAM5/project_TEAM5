function toggleTask(checkbox) {
    var span = checkbox.nextElementSibling;
    var fullList = checkbox.parentElement.parentElement;
    console.log(fullList);
    if (checkbox.checked) {
        span.classList.add('line-through');
        fullList.classList.remove('bg-gray-100');
        fullList.classList.add('bg-success');
    } else {
        span.classList.remove('line-through');
        fullList.classList.add('bg-gray-100');
        fullList.classList.remove('bg-success');
    }
}