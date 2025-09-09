/**
 * 
 */
const xlsx = require('xlsx');
const fs = require('fs');

/** 문서를 읽어 첫 번째 시트를 가져옴 
 * 
 */
const workbook = xlsx.readFile('기상청41_단기예보 조회서비스_오픈API활용가이드_격자_위경도(2411).xlsx');
const sheet = workbook.Sheets[workbook.SheetNames[0]];
const rows = xlsx.utils.sheet_to_json(sheet, { default: null});


/** 한글 컬럼명을 영어 약어로 매핑 
 * 
 */
const MAP = {
  '구분':               'type',
  '행정구역코드':      'admCd',
  '1단계':             'lvl1',
  '2단계':             'lvl2',
  '3단계':             'lvl3',
  '격자 X':            'gridX',
  '격자 Y':            'gridY',
  '경도(시)':          'lonDeg',
  '경도(분)':          'lonMin',
  '경도(초)':          'lonSec',
  '위도(시)':          'latDeg',
  '위도(분)':          'latMin',
  '위도(초)':          'latSec',
  '경도(초/100)':      'lonDec',
  '위도(초/100)':      'latDec',
  '위치업데이트':      'upd'
};


/** 매핑을 적용 
 * 
 */
const out = rows.map(r => {
  const o = {};
  
  for(const [kor, eng] of Object.entries(MAP)){
    o[eng] = r[kor];
  }

  return o;
})


/** 결과를 JSON 파일로 저장 
 * 
 * 
 */
fs.writeFileSync('WeatherOpenAPIAppendixDoc.json', JSON.stringify(out, null, 2), 'utf-8');
console.log(`총 ${out.length} 개의 행을 JSON 형식으로 변환하였습니다.`);