// function deleteEmployer(employerName) {
//     fetch('http://localhost:7000/main?taskName=' + taskName + '&duration_day=' +duration_day+ '&date='+ date, {
//         method: 'Delete',
//     }).then(res => window.location.reload = window.location.reload(true));
// }
// function  addPitem(taskName, duration_day, date) {
//     fetch('http://localhost:7000/main?taskName=' + taskName + '&duration_day=' +duration_day+ '&date='+ date, {
//         method: 'Post',
//     }).then(res => window.location.reload = window.location.reload(true));
// }

// let delButtons = document.querySelectorAll("li > button")
// Array.prototype.forEach.call(delButtons, function(button) {
//     button.addEventListener('click', deleteEmployer.bind(null, button.id));
// });
let currentList = null;
document.getElementById("submit_add_task").addEventListener('click', function () {
    if (currentList && validateTaskDuration()){
        taskName = document.getElementById("taskName").value;
        dueDay = document.getElementById("dueDay").value;
        date_string = document.getElementById("date").value;
        duration = document.getElementById("duration").value;
        console.log("clicked")
        console.log(taskName)
        console.log(dueDay)
        console.log(date_string)
        console.log(duration)
        fetch('http://localhost:7000/addTask?listId=' + currentList + '&taskName=' + taskName + '&dueDay=' +dueDay+ '&date='+ date_string
            + '&duration=' + duration, {
            method: 'Post',
        })
            .then(res => showTaskInList(currentList))
        ;}
}
);
document.getElementById("submit_add_list").addEventListener('click', function () {
        // if (validateTaskDuration()){
            listName = document.getElementById("listName").value;
            // userid = document.getElementById("userid").value;
            colabidstring = document.getElementById("colabidstring").value;
            console.log("clicked")
            console.log(colabidstring)
            // fetch('http://localhost:7000/addList?listName=' + listName + '&userid=' + userid + '&colabidstring=' + colabidstring, {
            //     method: 'Post',
            // });
            fetch('http://localhost:7000/addList?listName=' + listName + '&colabidstring=' + colabidstring, {
                method: 'Post',
            });
                // .then(res => window.location.reload = window.location.reload(true))
            ;}
    // }
);

for (let i of document.querySelectorAll(".delete_task"))
{
    i.addEventListener('click', function (event) {
        taskName = event.target.parentElement.parentElement.firstElementChild.innerHTML
        console.log("clicked")
        console.log(taskName)
        // console.log(duration_day)
        // console.log(date_string)
        fetch('http://localhost:7000/main?taskName=' + taskName , {
            method: 'Delete',
        }).then(res => window.location.reload = window.location.reload(true));
    })
}
for (let i of document.querySelectorAll(".delete_list"))
{
    i.addEventListener('click', function (event) {
        listId = event.target.parentElement.parentElement.firstElementChild.id.substring("taskList-id-".length);
        console.log("clicked")
        if (listId==="1"){
            alert("Sorry, you may not delete fist list");
            return;
        }
        console.log(listId)
        // console.log(duration_day)
        // console.log(date_string)
        fetch('http://localhost:7000/deleteList?listId=' + listId , {
            method: 'Delete',
        }).then(res => window.location.reload = window.location.reload(true));
    })
}

function deleteTask(taskName) {
    console.log("clicked")
    console.log(taskName)
    console.log(currentList)
    // console.log(duration_day)
    // console.log(date_string)
    fetch('http://localhost:7000/deleteTask?listId=' + currentList + '&taskName=' + taskName , {
        method: 'Delete',
    }).then(res => window.location.reload = window.location.reload(true));
}
// show details of task
function showDetail(taskName) {
    location.href = './showDetail?listId=' + currentList + '&taskName=' + taskName;
}

const showTaskInList = listId => {
    console.log(`listId=${listId}`)
    fetch('http://localhost:7000/showList?listId=' + listId)
        .then(res => res.json())
        .then(json => {
            document.getElementById('all-tasks').innerHTML = '';
            for (let task of json["taskList"]) {
                let html = `<tr>
<td>${task['taskName']}</td>
<td>Default Project</td>
<td>${task['duration_day']}</td>
<td>${task['date']}</td>
<td>${task['duration']}</td>
<td><button class="delete_task btn btn-fail" onclick="deleteTask('${task['taskName']}')" type="button" >Delete Task</button></td>
<!--show detail of a task-->
<td><button class="delete_task btn btn-fail" onclick="showDetail('${task['taskName']}')" type="button" >Task Detail</button></td>
</tr>
`
                document.getElementById('all-tasks').innerHTML += html;
            }
            currentList = listId;
        });
}

for (let i of document.querySelectorAll(".list-row")) {
    i.addEventListener('click', function (event) {
        let myId = event.target.id.substring("taskList-id-".length);
        showTaskInList(myId);
    });
}

window.addEventListener('DOMContentLoaded', e => {
    showTaskInList(currentList || 1);
})

// document.querySelectorAll(".delete_task").item(0)
// document.querySelectorAll(".delete_task").item(0).addEventListener('click', function (event) {
//     taskName = event.target.parentElement.parentElement.firstElementChild.innerHTML
//     console.log("clicked")
//     console.log(taskName)
//     // console.log(duration_day)
//     // console.log(date_string)
//     fetch('http://localhost:7000/main?taskName=' + taskName , {
//         method: 'Delete',
//     });
// });
//
function validateTaskDuration() {
    const taskName = document.getElementById("taskName").value;
    if (taskName ==="" ) {
        alert("task name cannot be null!");
        return false;

    }
    return true;
//
}
function validateListName() {
    const listName = document.getElementById("listName").value;
    if (taskName ==="" ) {
        alert("list name cannot be null!");
        return false;

    }
    return true;
//
}

document.getElementById("submit_add_list").addEventListener('click', function () {
        const listName = document.getElementById("listName").value;
        if (validateListName()){
            fetch('http://localhost:7000/main?listNamee=' + listName , {
                method: 'Post',
            }).then(res => window.location.reload = window.location.reload(true));}
    }
);
