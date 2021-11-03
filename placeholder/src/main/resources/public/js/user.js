document.getElementById("save_info").addEventListener('click', function () {
        firstname    = document.getElementById("user_firstname").value;
        lastname     = document.getElementById("user_lastname").value;
        organization = document.getElementById("user_organization").value;
        status       = document.getElementById("user_status").value;
        console.log("clicked")
        console.log(firstname)
        fetch('http://localhost:7000/userprofile?firstName='+ firstname +
            '&lastName=' + lastname +
            '&status=' + status +
            '&organization=' + organization, {
            method: 'Put',
        }).then(res => window.location.reload = window.location.reload(true));
    }
);

document.getElementById("save_summary").addEventListener('click', function () {
        summary = document.getElementById("user_summary").value;
        console.log("clicked")
        console.log(summary)
        fetch('http://localhost:7000/userprofile?summary='+ summary, {
            method: 'Put',
        }).then(res => window.location.reload = window.location.reload(true));
    }
);