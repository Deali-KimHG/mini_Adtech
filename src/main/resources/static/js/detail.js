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

    document.getElementById("title").removeAttribute("hidden");
    document.getElementById("price").removeAttribute("hidden");
    document.getElementById("updateImage").removeAttribute("hidden");
    document.getElementById("advertiseStartDate").removeAttribute("hidden");
    document.getElementById("advertiseEndDate").removeAttribute("hidden");
}
function updateCancel() {
    document.getElementById("btn-update").setAttribute("hidden", "hidden");
    document.getElementById("btn-update-cancel").setAttribute("hidden", "hidden");
    document.getElementById("btn-update-mode").removeAttribute("hidden");

    document.getElementById("title").setAttribute("hidden", "hidden");
    document.getElementById("title-content").value = document.getElementById("title-content").defaultValue;
    document.getElementById("price").setAttribute("hidden", "hidden");
    document.getElementById("price-content").value = document.getElementById("price-content").defaultValue;
    document.getElementById("updateImage").setAttribute("hidden", "hidden");
    document.getElementById("image-content").value = document.getElementById("image-content").defaultValue;
    document.getElementById("advertiseStartDate").setAttribute("hidden", "hidden");
    document.getElementById("advertiseStartDate-content").value = document.getElementById("advertiseStartDate-content").defaultValue;
    document.getElementById("advertiseEndDate").setAttribute("hidden", "hidden");
    document.getElementById("advertiseEndDate-content").value = document.getElementById("advertiseEndDate-content").defaultValue;
}