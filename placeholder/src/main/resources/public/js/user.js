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

let userId = document.cookie
    .split('; ')
    .find(row => row.startsWith('userid='))
    .split('=')[1];
// document.getElementById("imageid").src="../template/save.png";
document.getElementById("profile_image").addEventListener('click', function () {
    console.log(document.getElementById("profile_image").src)
});
document.getElementById("image1").addEventListener('click', function () {
    console.log("clicked");
    let source = document.getElementById("image1").src;
    console.log(source)
    fetch('http://localhost:7000/userprofile?profileImage='+ source, {
        method: 'Put',
    }).then(res => window.location.reload = window.location.reload(true));
});
document.getElementById("image2").addEventListener('click', function () {
    console.log("clicked");
    let source = document.getElementById("image2").src;
    console.log(source)
    fetch('http://localhost:7000/userprofile?profileImage='+ source, {
        method: 'Put',
    }).then(res => window.location.reload = window.location.reload(true));
});
document.getElementById("image3").addEventListener('click', function () {
    console.log("clicked");
    let source = document.getElementById("image3").src;
    console.log(source)
    fetch('http://localhost:7000/userprofile?profileImage='+ source, {
        method: 'Put',
    }).then(res => window.location.reload = window.location.reload(true));
});
document.getElementById("image4").addEventListener('click', function () {
    console.log("clicked");
    let source = document.getElementById("image4").src;
    console.log(source)
    fetch('http://localhost:7000/userprofile?profileImage='+ source, {
        method: 'Put',
    }).then(res => window.location.reload = window.location.reload(true));
});