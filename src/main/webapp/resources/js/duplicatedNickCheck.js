// 초기화
 // CSRF 토큰 변수, GET 요청 시에만 제외 하고 나머지 요청 시 CSRF 토큰을 함께 전송
// const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
// const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
 // join.jsp
const id = document.getElementById('id');
const nick = document.getElementById('nickName');
let joinRegBtn = document.getElementById('joinRegBtn');
const idCheckBtn = document.getElementById('idCheckBtn');
const nickCheckBtn = document.getElementById('nickCheckBtn');
 // 가입하기 버튼 활성화를 위한 변수
let isValidId = false;
let isValidNick = false;

// 중복되지 않는 경우에만 가입하기 버튼 활성화
// joinRegBtn.disabled = !(isValidId && isValidNick);
console.log(joinRegBtn);
console.log(!(isValidId && isValidNick));

/* 비동기 전송

  > 비동기 전송 시, url 과 config 가 필요하며 쿼리스트링으로 전송하는 경우에는 
    body 에 담을 필요가 없고 객체로 전송하는 경우에는 body 에 담아야 함

  > Request Body에 JSON을 담아 보내는 경우에만 headers 에 
    'Content-Type: application/json' 를 지정 

    # 바디가 없는 GET 요청이나, FormData, URLSearchParams 등을 보낼 때는 
      브라우저가 알아서 적절한 헤더를 달아 주거나 아예 바디가 없으니 
      따로 지정할 필요가 없음

    # 'Content-Type' : application/jso; 은 이 요청의 바디(body) 에 
      JSON 이 들어있다는 걸 서버에 알려 주는 역할
    
    # charset=utf-8 은 JSON.stringift() 시 변환할 문자를 지정
    
    # JSON.stringift() 시 변환할 문자를 지정하여 객체를 JSON 문자열로 변환하고 
      replacer/space 옵션을 통해 변환 규칙이나 들여쓰기를 제어할 수 있음

  ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
  > 비동기 전송 - Request Body에 JSON을 담아 보내는 경우의 예시 
    let uvo = {
      id : id.value,
      email : email.value,
      pwd : pwd.value,
      nickname : nickName.value,
      terms : terms.value
    }
    ...
    async function postUserToServer(uvo){
      try{
        const url = "/user/update"

        const config = {
          method : 'post',
          'CotentType-Type' : 'application/json; charset=utf-8'
        },
        // 세미 콜론 작성 X
        body : JSON.stringify(uvo)

        // 비동기 요청
        const resp = await fetch(url, config);
        const result = await resp.text();
    
        return result;
        
      }catch(err){
        console.log(err);
      }      
    }

  ―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
  > GET 비동기 요청에는 CSRF 토큰을 넣지 않아도 되지만, 
    POST, PUT, DELETE 의 비동기 요청에는 꼭 CSRF 토큰을 넣어야 함 

  > 쿼리 스트링으로 보내는 경우 단순 경로만 필요하다면 ? 사용 할 필요가 없고
    변수를 파라미터 형태로 붙이는 경우에만 사용하면 됨 
*/   
idCheckBtn.addEventListener("click", () => {
  if(id.value == "") { alert('ID 를 입력하세요 !'); return; }
 
  idCheckToServer(id.value).then(result => {
    
    if(result == "pass" && id.value != ""){ 
      isValidId = true; 
      alert('사용 가능한 ID 입니다.');
      joinRegBtn.disabled = !(isValidId && isValidNick);
      console.log(isValidId && isValidNick);
    } else {
      alert('사용 불가능한 ID 입니다.')
    }
    console.log(result);
  })
})


nickCheckBtn.addEventListener('click', () => {
	if(nickName.value == "") { alert('닉네임을 입력하세요 !'); return; }

  nickCheckToServer(nick.value).then(result =>{
  	
  
    if(result == "pass" && nickName.value != ""){ 
      isValidNick = true; 
      alert('사용 가능한 닉네임입니다.');
      joinRegBtn.disabled = !(isValidId && isValidNick);
      console.log(isValidId && isValidNick);
    }
    else{
      alert('사용 불가능한 닉네임입니다.');
    }
    console.log(result);
  })
})


// ID 중복 검사를 비동기로 요청 
async function idCheckToServer(id) {
  try {
    const url = `/user/checkid?id=${id}`;
    
    /* fetch(url, config)
    
    > fetch(url, config) 에서 method = 'get' 이 config 의 기본 설정이므로 
    config 생략 가능
    
    */ 
    const resp = await fetch(url);
     // 서버가 텍스트를 리턴한다면 .text(), JSON이면 .json() 사용
    const result = await resp.text();
    
    return result;

  } catch (error) {
    console.log(error);
  }
}


// nickName 중복 검사를 비동기로 요청 
async function nickCheckToServer(nick) {
  try {
    const url = `/user/checknick?nick=${nick}`;

    /* fetch(url, config)
      
      > fetch(url, config) 에서 method = 'get' 이 config 의 기본 설정이므로 
        config 생략 가능
    */ 
    const resp = await fetch(url);
     // 서버가 텍스트를 리턴한다면 .text(), JSON이면 .json() 사용
    const result = await resp.text();
    
    return result;

  } catch (error) {
    console.log(error);
  }
}