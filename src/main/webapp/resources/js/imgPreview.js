/**
 * 
 */
const fileInput = document.getElementById('fileInput');
const previewImg = document.getElementById('previewImg');

if(fileInput != null){
  fileInput.addEventListener('change', (e) =>{
    // 초기화
    const file = e.target.files[0];
     // FileReader API - JS 에서 파일을 읽는 객체로 파일을 읽고 클라이언트 컴퓨터에 파일 저장 가능
    const reader = new FileReader();
     // 파일 크기 제한 
    const maxSize = 1024 * 1024 * 20;

    /** if(!e.target.files || e.target.files.length == 0){ return; }
     * 
     * > 다음 if 문은 선택 취소 시, 기본 이미지로 복원하는 코드
     * 
     * > e.target = input, e.target.value = 업로드 파일 경로
     *   , e.target.files = 업로드된 파일의 정보가 담긴 배열
     */
    if(!e.target.files || e.target.files.length == 0){
      return;
    }

    // 파일 크기 제한
    if(file.size > maxSize){ alert('20MB 이하의 이미지를 선택해주세요'); return; }
    
    // 매개변수에 작성된 파일 (fileInput 의 img 파일) 을 읽어서 저장한 뒤 
    // 파일을 나타내는 URL 을 result 속성으로 얻어올 수 있게 함  
    reader.readAsDataURL(file);
  
    // 파일을 다 읽으면
    reader.onload = (e) => {
      // 확인
      console.log(e.target.result);

      // fileInput 의 src 속성에 저장할 url
      const url = e.target.result;
      previewImg.setAttribute("src", url);
    }
  });
}