/**
 * 
 */
console.log("boardRegisterFile!");

document.getElementById('trigger').addEventListener('click', () => {
  document.getElementById('file').click();

})

/** File 에 대한 정규표현식 작성 
 * 
 *  > 조건 1) 실행파일은 첨부 불가
 * 
 *  > 조건 2) 20MB 이상은 업로드 금지 
 * */ 
const regExp = new RegExp("\.(exe|jar|msi|dll|sh|bat)$");
const maxSize = 1024 * 1024 * 20;

function fileValid(fileName, fileSize){
  if(regExp.test(fileName)){ return 0; }
  else if(fileSize > maxSize){ return 0; }
  else { return 1; }
}

/** addEventListener('change')
 * 
 * 
 */

document.addEventListener('change', (e) => {
  if(e.target.id == 'file'){
    // files - input type = "file" 인 경우 input 에 저장된 file 정보를 가져오는 속성 
    console.log()  

    const fileObj = document.getElementById('file').files;
    console.log(fileObj);
    
    // 내가 등록한 파일의 목록을 fileZone 영역에 추가 
     // Valid 한 정보를 같이 표시 
    const div = document.getElementById('fileZone');
    div.innerHTML = "";
    document.getElementById('regBtn').disabled = false;

    // 조건을 만족하는 File 인지 확인하는 변수 
     // 모든 파일이 조건을 만족해야 함 
     // * (곱하기) 를 통해 모든 파일이 조건을 만족하는지 확인
     let isOk = 1;

    /** HTML 출력 형식
     * 
     * <ul class="list-group list-group-flush" id="cmtListArea">
		    <li class="list-group-item">
          <div class="mb-3">
            <div class="fw-bold">업로드 가능여부</div>
            파일 이름
          </div>
          <span class="badge text-bg-primary">파일 크기</span>
        </li> 
      </ul>
     */
    let ul = `<ul class="list-group list-group-flush">`;
    // 개별 파일에 대한 검증과 결과 표시
    for(let f of fileObj){
      // 개별 결과
      let validRes = fileValid(f.name, f.size);
      // 전체 누적
      isOk *= validRes;
      
      ul += `<li class="list-group-item">`;
      ul += `<div class="mb-3">`;
      ul += `${validRes ? '<div class="fw-bold" style ="color : blue;">업로드 가능</div>' : 
            '<div class="fw-bold" style = "color : red;">업로드 불가능</div>'
      }`;
      ul += `${f.name}`;
      ul += `</div>`;
      ul += `<span class="badge text-bg-primary">${f.size} Bytes </span>`;
      ul += `</li>`;
    }
    ul += `</ul>`;

    div.innerHTML = ul;
    if(isOk == 0){
      // 하나라도 불만족하는 파일이 있는 경우 등록버튼 비활성화 
      document.getElementById('regBtn').disabled = true;
    }
  }
})