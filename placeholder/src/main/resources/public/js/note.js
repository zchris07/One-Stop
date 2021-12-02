document.getElementById("submit_notes").addEventListener('click', function () {
        taskName = document.getElementById("noteTitle").innerText;
        taskNote = document.getElementById("taskNote").value;
        if (document.getElementById("fixGrammar").checked) {
            isCheckedGrammar = "Yes"
        }
        else {
            isCheckedGrammar = "No"
        }
        if (document.getElementById("fixSpelling").checked) {
            isCheckedSpelling = "Yes"
        } else{
            isCheckedSpelling = "No"
        }
        if (document.getElementById("fixCapital").checked) {
            isCheckedCapital = "Yes"
        } else {
            isCheckedCapital = "No"
        }
        if (document.getElementById("fixLongRunning").checked) {
            isCheckedLongRunning = "Yes"
        } else {
            isCheckedLongRunning = "No"
        }
        console.log("clicked")
        console.log(taskName)
        console.log(taskNote)
        console.log(isCheckedGrammar)
        console.log(isCheckedSpelling)
        console.log(isCheckedCapital)
        console.log(isCheckedLongRunning)
        // /addNotes?taskName=good&taskNote=dfa
        fetch(path+'addNotes?taskName='+ taskName + '&taskNote=' + taskNote +
            '&isCheckedGrammar=' + isCheckedGrammar+
            '&isCheckedSpelling=' + isCheckedSpelling+ '&isCheckedCapital=' + isCheckedCapital
            +'&isCheckedLongRunning=' + isCheckedLongRunning, {
            method: 'Put',
        }).then(res => window.location.reload = window.location.reload(true));
    }
);
