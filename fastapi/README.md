## 사전 준비

### conda를 이용해서 가상환경 생성 (또는 python venv)

- conda 사용하기 위해서는 anaconda가 먼저 설치되어 있어야 함

```python
conda create -n fastapi
```

### 가상환경 activate

```python
conda activate fastapi
```

### conda activate가 안 될 경우에는 아래 명령어를 실행

```python
source ~/anaconda3/etc/profile.d/conda.sh
```

### 라이브러리 다운로드

```python
pip install -r requirements.txt
```

### 서버 실행

```python
uvicorn main:app --reload
```


## 요청 URL

### Connection Test

- http://<현재 서버 도메인>:8000

```python
{"message": "FastAPI Connection Test"}
```

### 패션 아이템 속성 분류

- http://<현재 서버 도메인>:8000/closet

```python
{
    "categories": [
        "니트웨어"
    ],
    "details": [
        "니트꽈베기"
    ],
    "textures": [
        "우븐",
        "니트"
    ]
}
```

- Spring Boot 예시 코드

```
public ResponseEntity<ClosetUploadResponseDTO> closetAttributeClassifier(String storeFilePath, MultipartFile clothImage) {
    // RestTemplate 인스턴스 생성
    RestTemplate restTemplate = new RestTemplate();

    // 파일 객체 생성
    File file = new File(storeFilePath);

    byte[] fileBytes;

    try {
        fileBytes = clothImage.getBytes();
    } catch (IOException exception) {
        throw new ClosetImageIOException("옷 이미지가 저장되어 있지 않습니다.");
    }

    // ByteArrayResource를 사용하여 파일을 변환
    ByteArrayResource byteArrayResource = new ByteArrayResource(fileBytes) {
        @Override
        public String getFilename() {
            return file.getName();
        }
    };

    MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
    bodyMap.add("file", byteArrayResource);

    // HTTP 요청 헤더 설정
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    // HTTP 요청 엔티티 생성
    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

    // FastAPI 서버에 POST 요청 보내기
    return restTemplate.postForEntity(FAST_API_ENDPOINT, requestEntity, ClosetUploadResponseDTO.class);
}
```
