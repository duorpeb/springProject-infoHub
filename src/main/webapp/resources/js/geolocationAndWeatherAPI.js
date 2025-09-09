console.log("============================ geolocaitonApi.js ============================");
// console.log(weatherJson);

// 위치 비허용 시 값 확인을 위한 코드
navigator.geolocation.getCurrentPosition((location) => {
  console.log(location.coords.latitude);
  console.log(location.coords.longitude);
  console.log(typeof location.coords.latitude);
  console.log(typeof location.coords.longitude);
})


/** Geolocation API
 * 
 *  > Geolocation API는 navigator.geolocation 객체를 통해 아래와 같이 사용할 수 있음 
 *  
 * 
 *  > navigator.geolocation.getCurrentPosition(success, error, [options])
 * 
 *    # success : GeolocationPosition 객체를 유일한 매개변수로 받는 콜백 함수
 *  
 *    # error (Option) : GeolocationPositionError 객체를 유일한 매개변수로 받는 콜백 함수
 * 
 *    # options 는 현재 위치를 가져오는데 3가지의 옵션 (enableHighAccuracy, timeout
 *      , maximumAge ) 을 정의할 수 있는 파라미터 
 * 
 *    # options 의 enableHighAccuracy는 고정밀 위치 정보를 요청하는지에 대한 값으로 
 *      default 값은 false 
 * 
 *    # timeout은 현재위치를 가져오기 위한 시간 제한 옵션으로 기본값은 무한대이며
 *      ms단위로 시간을 제한할 수 있음
 * 
 *    # maximumAge 는 캐시된 위치 정보의 유효기간을 정의할 수 있는 옵션으로 기본값은 0 
 *      이며 캐시된 값 이내에 위치 정보가 있으면 해당 값을 반환하고 새로운 위치를 
 *      요청하지 않음 
 * 
 * ￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣
 * > navigator.geolocation.watchPosition(success, error, [options])
 * 
 *    # 장치의 이동이나 위치 정밀도 향상으로 인해 위치 정보가 바뀔 때 호출할 콜백 함수를 
 *      watchPosition() 메서드로 설정할 수 있으며 해당 메서드의 매개변수는 
 *      getCurrentPosition() 와 동일 
 * 
 * ￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣
 * > 위치 비허용시 undefined
 */

// 초기화
 // 현재 위치를 표시하는 HTML (h6)
const weatherInlocation = document.querySelector('.locationInWeather');

document.addEventListener('DOMContentLoaded', () => {
   /** 위경도 초기화
    * 
    *  > navigator.geolocation.getCurrentPosition가 비동기로 동작해 즉시 리턴하기 때문에 
    *    위치 정보 접근을 비허용하는 경우 getCurrentPosition() 을 호출한 블록 밖에서 재할당하는
    *    구문은 실행되지 않음 
    * 
    *  > 즉, getCurrentPosition() 을 호출한 블록안에서 모든 처리를 해야 함
    * 
    * */ 
  navigator.geolocation.getCurrentPosition(location => {
    console.log("============================ geolocaitonApi.js >>>>> isValidLocation ============================");
    console.log(location);
    // 초기화
     // 위도, 경도 
    const geoLat = location.coords.latitude;
    const geoLong = location.coords.longitude;
     // 지역명
    let regionName = "";
    
    const { gridX, gridY } = latLonToGrid(geoLat, geoLong);
    // 확인
    console.log(`gridX : ${gridX}, gridY : ${gridY}`);


    /**  WeatherOpenAPIAppendixDoc.js 에서 격자 X,Y 좌표에 해당하는 지역명 가져오기 
     * 
     *  > 해당 격자 X,Y 좌표에 해당하는 지역이 2개 이상인 경우 location.coords.latitude
     *    .toString().substring(0,6) 을 이용해 idx.latDec 와 비교
     * 
     *  > 기상청 단기예보 API는 “격자(X,Y) 단위”로 예보가 제공되며 동일한 격자 좌표에 속하는 지점들은 
     *    API 상에서는 모두 같은 예보 값을 돌려줌
     * 
     * */
     // Geolocation API 의 위도(초) 초기화
    const comGeoLong = geoLong.toString().substring(0,6); // 확인, 
    console.log(comGeoLong);
     

    /** 기상청 단기예보 API 의 별도 첨부 문서에서 Geolocation API 를 통해 얻어낸 정보와 일치하는
        지역명과 날씨정보 가져오기
      * 
      * */
     // 지역명 초기화
    for(let idx of weatherJson){
      if(idx.gridX == gridX && idx.gridY == gridY){
        if(idx.lonDec.toString().substring(0,6) == comGeoLong){
          // 확인
          console.log(idx.latDec.toString().substring(0,6));

          regionName = `${idx.lvl1} ${idx.lvl2} ${idx.lvl3}`;
          console.log(regionName);

          weatherInlocation.innerText = `현재 위치 : ${regionName}`;
        }
      }
    }

    
    /** getWeatherInfoFromServer(gridX, gridY) - Server 에서 4일간의 날씨 정보 가져오기
     *  
     *  > weatherInfoIndayPrintDiv 에 표현해야 할 것 
     *   - pop : 강수확률     - pty : 강수형태 (맑음, 비, 흐림 등의 기상상태)
     * 
     *   - tmn : 일 최저기온     - tmx : 일 최고기온
     * 
     * */ 
    getWeatherInfoFromServer(gridX, gridY).then(result => {
      // 확인 
      console.log("================ 4일간 날씨 정보 ================");
      console.log(result);

      // 초기화
       // weather-img-div, weather-txt-div 에 HTML 요소 삽입을 위한 초기화
      let cnt = 0;

      // 형식에 따른 출력 조정 
      result.forEach(w => {
        // image DocumentFragment 생성
        const imgFrag = document.createDocumentFragment();
        
        // txt DocumentFragment 생성 
        const txtFrag = document.createDocumentFragment(); 

        // <img> 생성 
        const img = document.createElement('img');
    
        // 기상 상태에 따른 이미지를 서버에서 가져오기 
        switch(w.pty){
          case "없음" : 
            switch(w.sky){
              case "맑음" : 
                img.src = `${imgDir}/resources/img/${encodeURIComponent('weather-sunny.png')}`;
                break;
                
              case "구름많음" :
                img.src = `${imgDir}/resources/img/${encodeURIComponent('weather-nebulousness.png')}`;
                break;
    
              case "흐림" :
                img.src = `${imgDir}/resources/img/${encodeURIComponent('weather-cloudy.png')}`;
                break;
            }
            break;
          
          case "비" : 
            img.src = `${imgDir}/resources/img/${encodeURIComponent('weather-rain.png')}`;
            break;
    
          case "비/눈" : 
            img.src = `${imgDir}/resources/img/${encodeURIComponent('weather-rain-and-snow.png')}`;
            break;
    
          case "눈" : 
            img.src = `${imgDir}/resources/img/${encodeURIComponent('weather-snow.png')}`;
            break;
    
          case "소나기" : 
            img.src = `${imgDir}/resources/img/${encodeURIComponent('weather-rain.png')}`
            break;
        } 
        
        imgFrag.appendChild(img);

        // 강수확률, 일 최저 · 최고 기온 표시 
         // 강수확률 
        const div1 = document.createElement('div');
        div1.innerHTML = `강수확률 : ${w.pop} %`;
        
         // 일 최저 기온 
        const div2 = document.createElement('div');
        div2.innerHTML = `일 최저기온 : ${w.tmn} ℃`;
        
        // 일 최고 기온
        const div3 = document.createElement('div');
        div3.innerHTML = `일 최고기온 : ${w.tmx} ℃`;

        txtFrag.appendChild(div1);
        txtFrag.appendChild(div2);
        txtFrag.appendChild(div3);

        document.getElementsByClassName('weather-img-div')[cnt].appendChild(imgFrag);
        document.getElementsByClassName('weather-txt-div')[cnt].appendChild(txtFrag);

        cnt++;
      })
    })
      
    
    
    
  }, 
  err => { 
    console.log("위치 조회 요청 거절", err);

    weatherInlocation.innerText = `설정된 위치 : 서울 특별시`;
    
    
    getWeatherInfoFromServer(61, 125).then(result =>{

    }) 
  },
  { enableHighAccuracy : true });

}) // DOMContentLoaded fin



/** getWeatherInfoFromServer(gridX, gridY) - 단기예보조회서비스API 를 활용해 4일간의 날씨 정보를 가져옴
 * 
 *  > latLonToGrid(lat, lon) 을 활용해 추출한 gridX, gridY 를 서버의 getWeather(pageNo, nx, ny) 의 
 *    파라미터로 전달하여 4일간의 날씨를 가져오는 함수
 *    
 */
async function getWeatherInfoFromServer(gridX, gridY) {
  try {
    const resp = await fetch(`/weather/${gridX}/${gridY}`)

    const result = await resp.json();

    return result;

  } 
    catch (error) {
      console.log("=============== getWeatherInfoFromServer(gridX, gridY) ERROR ===============");

      console.log(error);
  }
}


/** latLonToGrid(lat, lon) - 기상청 격자 좌표 변환 함수 
    (Geolocation 에서 주는 위경도 → 기상천 단기에보 API 의 격자 X/Y 좌표)
 *
 *  > latLonToGrid(lat, lon) 은 기상청 “단기예보 조회 서비스” LCC 투영 공식
 *  
 * */ 
// 
function latLonToGrid(lat, lon) {
  const RE    = 6371.00877;           // 지구 반경 (km)
  const GRID  = 5.0;                  // 격자 간격 (km)
  const SLAT1 = 30.0 * Math.PI/180.0;  // 표준위도1 (rad)
  const SLAT2 = 60.0 * Math.PI/180.0;  // 표준위도2 (rad)
  const OLON  = 126.0 * Math.PI/180.0; // 기준점 경도 (rad)
  const OLAT  = 38.0  * Math.PI/180.0; // 기준점 위도 (rad)
  const XO    = 43;                   // 기준점 X 격자 좌표
  const YO    = 136;                  // 기준점 Y 격자 좌표

  const DEGRAD = Math.PI/180.0;

  // 리사이징 상수
  const re = RE / GRID;
  const slat1 = SLAT1, slat2 = SLAT2;
  const olon  = OLON,  olat  = OLAT;

  // 투영 상수 계산
  let sn = Math.log(Math.cos(slat1)/Math.cos(slat2))
         / Math.log(
             Math.tan(Math.PI*0.25 + slat2*0.5) /
             Math.tan(Math.PI*0.25 + slat1*0.5)
           );
  let sf = Math.pow(Math.tan(Math.PI*0.25 + slat1*0.5), sn)
         * Math.cos(slat1) / sn;
  let ro = re * sf / Math.pow(Math.tan(Math.PI*0.25 + olat*0.5), sn);

  // 입력 위경도 → 투영
  let ra = re * sf / Math.pow(
             Math.tan(Math.PI*0.25 + (lat * DEGRAD)*0.5),
             sn
           );
  let theta = (lon * DEGRAD) - olon;
  if (theta > Math.PI)  theta -= 2.0 * Math.PI;
  if (theta < -Math.PI) theta += 2.0 * Math.PI;
  theta *= sn;

  // 최종 격자 좌표 (반올림)
  const x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
  const y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);

  return { gridX: x, gridY: y };
}


