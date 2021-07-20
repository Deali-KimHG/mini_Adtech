window.onload = function() {
    let today = new Date();
    let dd = today.getDate();
    let MM = today.getMonth() + 1;
    let yyyy = today.getFullYear();
    let HH = today.getHours();
    let mm = today.getMinutes();

    if(dd < 10)
        dd = '0' + dd;
    if(MM < 10)
        MM = '0' + MM;

    today = yyyy + '-' + MM + '-' + dd + 'T' + HH + ':' + mm;
    document.getElementById("advertiseStartDate").setAttribute("min", today);
    document.getElementById("advertiseEndDate").setAttribute("min", today);
}

function update() {
    let form = $('#detailForm')[0];
    let data = new FormData(form);
    let id = $('#updateId').val();

    $.ajax({
        type: "PUT",
        url: "/core/v1/creative/" + id,
        enctype: 'multipart/form-data',
        data: data,
        contentType: false,
        processData: false,
        cache: false
    }).done(function() {
        alert("수정 성공");
        location.href = "/detail/" + id;
    }).fail(function(err) {
        let respErr = err.responseJSON;
        console.log(respErr);
        if(respErr.status === 400){
            let errors = respErr.errors;
            let str = "입력값 오류 목록\n";
            for(let i = 0; i < errors.length; i++) {
                str += "- " + errors[i].reason + "\n";
            }
            alert(str);
        } else if(respErr.status === 401) {
            alert("종료일시가 시작일시보다 과거의 시간이면 안됩니다");
        } else if(respErr.status === 402) {
            alert("시작일시의 변경은 현재 시간이나 미래의 시간으로 설정해야 합니다.");
        } else if(respErr.status === 403) {
            alert("종료일시의 변경은 현재 시간이나 미래의 시간으로 설정해야 합니다.");
        } else if(respErr.status === 404) {
            alert("만료된 광고의 종료일시는 현재 시간으로 설정할 수 없습니다.");
        } else {
            alert(err.responseJSON.message);
        }
    })
}
function pause() {
    let id = $('#updateId').val();

    $.ajax({
        type: "GET",
        url: "/core/v1/creative/pause/" + id
    }).done(function() {
        alert("광고를 일시정지하였습니다.");
        location.href = '/detail/' + id;
    }).fail(function(err) {
        alert(err.responseJSON.message);
    })
}
function pauseCancel() {
    let id = $('#updateId').val();

    $.ajax({
        type: "GET",
        url: "/core/v1/creative/restart/" + id
    }).done(function() {
        alert("광고의 일시정지를 해제합니다.");
        location.href = '/detail/' + id;
    }).fail(function() {
        alert(err.responseJSON.message);
    })
}
function deleteCreative() {
    let confirm = window.confirm("정말로 삭제하시겠습니까?");
    let id = $('#updateId').val();

    if(confirm){
        $.ajax({
            type: "DELETE",
            url: "/core/v1/creative/" + id
        }).done(function() {
            alert("삭제가 성공하였습니다.");
            location.href = '/';
        }).fail(function(err) {
            alert(err.responseJSON.message);
        })
    } else {
        alert("삭제가 취소되었습니다.");
    }
}
function updateMode() {
    document.getElementById("btn-update").removeAttribute("hidden");
    document.getElementById("btn-update-cancel").removeAttribute("hidden");
    document.getElementById("btn-update-mode").setAttribute("hidden", "hidden");

    document.getElementById("title").removeAttribute("readonly");
    document.getElementById("price").removeAttribute("readonly");
    document.getElementById("image").removeAttribute("disabled");
    document.getElementById("advertiseStartDate").removeAttribute("readonly");
    document.getElementById("advertiseEndDate").removeAttribute("readonly");
}
function updateCancel() {
    document.getElementById("btn-update").setAttribute("hidden", "hidden");
    document.getElementById("btn-update-cancel").setAttribute("hidden", "hidden");
    document.getElementById("btn-update-mode").removeAttribute("hidden");

    document.getElementById("title").setAttribute("readonly", "readonly");
    document.getElementById("title").value = document.getElementById("title").defaultValue;
    document.getElementById("price").setAttribute("readonly", "readonly");
    document.getElementById("price").value = document.getElementById("price").defaultValue;
    document.getElementById("image").setAttribute("disabled", "disabled");
    document.getElementById("image").value = document.getElementById("image").defaultValue;
    document.getElementById("advertiseStartDate").setAttribute("readonly", "readonly");
    document.getElementById("advertiseStartDate").value = document.getElementById("advertiseStartDate").defaultValue;
    document.getElementById("advertiseEndDate").setAttribute("readonly", "readonly");
    document.getElementById("advertiseEndDate").value = document.getElementById("advertiseEndDate").defaultValue;
}