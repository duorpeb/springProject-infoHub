// <button type="submit"> 의 역할을 하는 코드
document.getElementById('lgLink').addEventListener('click',(e) => {
  // 기존 이벤트 지우기
  e.preventDefault();

  document.getElementById('lgForm').submit();
})