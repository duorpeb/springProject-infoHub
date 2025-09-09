document.addEventListener('DOMContentLoaded', () => {
  const imgErrMsg = document.getElementById('imgErrMsg');

  if (imgErrMsg.value == -1) {
    alert('이미지 파일만 업로드 가능합니다..!');
  }
})

document.getElementById('userDelBtn').addEventListener('click',() => {
	location.href = `/user/delete?id=${userId}`;
})

