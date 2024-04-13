import json
import csv
import os

def flatten_json(y):
    out = {}

    def flatten(x, name=''):
        if type(x) is dict:
            for a in x:
                if name + a in ['이미지 정보', '데이터셋 정보_파일 생성일자', '데이터셋 정보_파일 이름', '데이터셋 상세설명_렉트좌표', '데이터셋 상세설명_폴리곤좌표']:
                    continue  # 제외할 키는 건너뜁니다.
                flatten(x[a], name + a + '_')
        elif type(x) is list:
            i = 0
            for a in x:
                flatten(a, name + str(i) + '_')
                i += 1
        else:
            out[name[:-1]] = x

    flatten(y)
    return out

# 모든 파일을 처리하기 위한 메인 함수
def process_files(data_dir, output_csv):
    all_fieldnames = set()

    # 첫 번째 패스: 모든 필드명 수집
    for root, dirs, files in os.walk(data_dir):
        for file in files:
            if file.endswith('.json'):
                filepath = os.path.join(root, file)
                try:
                    with open(filepath, 'r', encoding='utf-8') as f:
                        print(f"Collecting fields from: {filepath}")
                        data = json.load(f)
                        flat = flatten_json(data)
                        flat = {k: v for k, v in flat.items() if '폴리곤좌표' not in k and '렉트좌표' not in k}
                        all_fieldnames.update(flat.keys())
                except Exception as e:
                    print(f"Error processing file {filepath}: {e}")

    # CSV 파일 준비
    with open(output_csv, 'w', newline='', encoding='utf-8') as csvfile:
        writer = csv.DictWriter(csvfile, fieldnames=all_fieldnames)
        writer.writeheader()

        # 두 번째 패스: 데이터 쓰기
        for root, dirs, files in os.walk(data_dir):
            for file in files:
                if file.endswith('.json'):
                    filepath = os.path.join(root, file)
                    try:
                        with open(filepath, 'r', encoding='utf-8') as f:
                            print(f"Processing file: {filepath}")
                            data = json.load(f)
                            flat = flatten_json(data)
                            flat = {k: v for k, v in flat.items() if '폴리곤좌표' not in k and '렉트좌표' not in k}
                            writer.writerow(flat)
                    except Exception as e:
                        print(f"Error processing file {filepath}: {e}")

# 데이터 처리 시작
data_dir = 'data'  # 데이터가 있는 디렉토리 경로
output_csv = 'output2.csv'  # 출력될 CSV 파일의 이름
process_files(data_dir, output_csv)
