/**
 * 
 */
console.log("boardModify !");

const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');

document.querySelectorAll('.fileDelBtn').forEach(btn => {
  console.log(btn);

  btn.addEventListener('click', async (e) => {
    console.log(e.target);

    let li = e.target.closest('li');
    let uuid = li.dataset.uuid;

    try {
      const resp = await fetch(`/board/fDel/${uuid}`, {
        method : "delete",
        headers : {
          [csrfHeader] : csrfToken
        },
      });
      
      if(resp.ok){ li.remove(); return resp.text(); }
      
    } catch (error) {
      console.log(error);
      console.log("첨부 파일 삭제 실패 !");
    }
  }) // btn.addEventListener fin 
})


// async function delFileToServer(uuid, bno) {
//   try {
//     const url = `/board/filedel?${uuid}&${bno}`

//     const config = {
//       method : 'delete'
//     }
    
//     const resp = await fetch(url, config);
//     const result = await resp.text();
    
//     return result;

//   } catch (error) {
//     console.log(error);
//   }
// }