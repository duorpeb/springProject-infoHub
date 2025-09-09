/** News API 
 *  
 * > Client ID - nrTHWl1GXAGLIvmacd4I, Client Secret - HbL30iOo34
 * 
 * > Category 는 home.jsp 에서 <a> 태그로 처리  
 */


/** 인피니티 스크롤 이벤트 리스너 
 * 
 *  > 바닥 (scroll-bottom) 감지용 요소 : <div id="infinite-scroll-end" style="height: 1px"></div>
 * 
 * */



/** readMore() - infinite Scroll 메서드 
 * 
 * */ 
async function readMore(){
  // if()
}


/** loadingSpinner() - 로딩 스피너 메서드  
 * 
 * 
 * */



/**  getListToServer() - 게시글 더 불러오는 메서드 */
async function getListToServer(pageNo = 1, qty = 4) {
  try {
    const res = await fetch(`/board/list?pageNo=${pageNo}&qty=${qty}`);

    const result = await res.json();

    return result;

  } 
    catch (error) {
      // 확인 
      console.log("=========================== getListToServer(pageNo, qty)'S ERROR ===========================");
      console.log(error);
  }
}