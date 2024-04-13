FROM nginx:alpine
RUN rm -rf /etc/nginx/conf.d/default.conf
COPY /nginx/nginx.conf /etc/nginx/conf.d

RUN rm -rf /usr/share/nginx/html/*
COPY /dist /usr/share/nginx/html

EXPOSE 80
ENTRYPOINT ["nginx", "-g", "daemon off;"]