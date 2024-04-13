FROM python:3.10.0

RUN python -m pip install --upgrade pip
RUN pip install torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cpu
RUN pip install uvicorn

WORKDIR /app/

COPY ./ /app/

RUN pip install -r requirements.txt

CMD uvicorn --host=0.0.0.0 --port 8000 main:app
