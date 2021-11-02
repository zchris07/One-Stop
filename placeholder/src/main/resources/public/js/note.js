document.getElementById("submit_notes").addEventListener('click', function () {
    taskName = document.getElementById("noteTitle").innerText;
    taskNote = document.getElementById("taskNote").value;
    console.log("clicked")
    console.log(taskName)
    console.log(taskNote)
    // /addNotes?taskName=good&taskNote=dfa
    fetch('http://localhost:7000/addNotes?taskName='+ taskName + '&taskNote=' + taskNote, {
        method: 'Put',
    }).then(res => window.location.reload = window.location.reload(true));
    }
);