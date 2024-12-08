function deletePosting() {
    const postingId = document.getElementById('deletePostingId').value;
    if (!postingId) {
        alert('게시글 ID를 입력해주세요.');
        return;
    }

    const token = localStorage.getItem('access');

    // 서버로 DELETE 요청 보내기 (게시글 삭제)
    fetch('/api/v1/admin/delete/posting', {
        method: 'DELETE',  // DELETE 메서드 사용
        headers: {
            'Content-Type': 'application/json',  // 요청 본문은 JSON 형식으로 보냄
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify({
            postingId: postingId
        })  // 본문에 게시글 ID를 포함
    })
        .then(response => {
            if (response.ok) {
                alert('게시글이 삭제되었습니다.');
            } else {
                alert('게시글 삭제에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('게시글 삭제 오류:', error);
            alert('게시글 삭제에 실패했습니다.');
        });
}


function restorePosting() {
    const postingId = document.getElementById('restorePostingId').value;
    if (!postingId) {
        alert('게시글 ID를 입력해주세요.');
        return;
    }

    const token = localStorage.getItem('access');

    fetch(`/api/v1/admin/restore/posting`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify({
            postingId: postingId
        })
    })
        .then(response => {
            if (response.ok) {
                alert('게시글이 복구되었습니다.');
            } else {
                alert('게시글 복구에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('게시글 복구 오류:', error);
            alert('게시글 복구에 실패하였습니다.');
        });
}



// 댓글 삭제
function deleteComment() {
    const commentId = document.getElementById('deleteCommentId').value;

    if (!commentId) {
        alert('댓글 ID를 입력해주세요.');
        return;
    }

    const token = localStorage.getItem('access');

    fetch('/api/v1/admin/delete/comment', {
        method: 'DELETE',  // DELETE 메서드 사용
        headers: {
            'Content-Type': 'application/json',  // 요청 본문은 JSON 형식으로 보냄
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify({
            commentId: commentId
        })  // 본문에 게시글 ID를 포함
    })
        .then(response => {
            if (response.ok) {
                alert('댓글이 삭제되었습니다.');
            } else {
                alert('댓글 삭제에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('댓글 삭제 오류:', error);
            alert('댓글 삭제에 실패했습니다.');
        });


}


function restoreComment(){
    const commentId = document.getElementById('restoreCommentId').value;
    if (!commentId) {
        alert('댓글 ID 입력해주세요.');
        return;
    }


    const token = localStorage.getItem('access');

    fetch(`/api/v1/admin/restore/comment`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify({
            commentId: commentId
        })
    })
        .then(response => {
            alert("hi");

            if (response.ok) {
                alert('댓글이 복구되었습니다.');
            } else {
                alert('댓글 복구에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('댓글 복구 오류:', error);
            alert('댓글 복구에 실패했습니다.');
        });

}


function registerManager(){

    console.log("Dd");

    const email = document.getElementById('email').value;

    console.log(email)

    const password = document.getElementById('password').value;
    const name = document.getElementById('name').value;
    const memberAddress = document.getElementById('memberAddress').value;
    const memberAddressDetail = document.getElementById('memberAddressDetail').value;
    const phoneNumber = document.getElementById('phoneNumber').value;
    const storeName = document.getElementById('storeName').value;
    const address = document.getElementById('address').value;
    const addressDetail = document.getElementById('addressDetail').value;


    console.log("등록");


    // 필수 입력값 확인
    if (!email || !password || !name || !memberAddress || !memberAddressDetail || !phoneNumber || !storeName || !address || !addressDetail) {
        alert('모든 필드를 입력해주세요.');
        return;
    }

    const token = localStorage.getItem('access');
    const url = '/api/v1/admin/register/manager'; // 실제 API 경로에 맞게 수정하세요.




    console.log("dd");

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify({
            email: email,
            password: password,
            name: name,
            memberAddress: memberAddress,
            memberAddressDetail: memberAddressDetail,
            phoneNumber: phoneNumber,
            storeName: storeName,
            address: address,
            addressDetail: addressDetail
        })
    })
        .then(response => {
            if (response.ok) {
                alert('매니저가 성공적으로 등록되었습니다.');
            } else {
                alert('매니저 등록에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('매니저 등록 오류:', error);
            alert('매니저 등록에 실패했습니다.');
        });
    console.log("dd");

}



function restoreMember() {
    const memberId = document.getElementById('restoreMemberId').value;
    if (!memberId) {
        alert('멤버 ID를 입력해주세요.');
        return;
    }

    const token = localStorage.getItem('access');

    fetch(`/api/v1/admin/restore/member`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify({
            memberId: memberId
        })
    })
        .then(response => {
            if (response.ok) {
                alert('회원이 복구되었습니다.');
            } else {
                alert('회원 복구에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('회원 복구 오류:', error);
            alert('회원 복구에 실패하였습니다.');
        });
}

