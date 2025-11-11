FROM ubuntu:22.04 AS build

# Install dependencies
RUN apt-get update && \
    apt-get install -y curl git wget unzip libgconf-2-4 gdb libstdc++6 \
    libglu1-mesa fonts-droid-fallback lib32stdc++6 python3 && \
    apt-get clean

# Install Flutter
RUN git clone https://github.com/flutter/flutter.git -b stable /usr/local/flutter
ENV PATH="/usr/local/flutter/bin:/usr/local/flutter/bin/cache/dart-sdk/bin:${PATH}"

# Enable web support
RUN flutter config --enable-web
RUN flutter doctor

WORKDIR /app

# Copy pubspec files
COPY frontend/pubspec.yaml frontend/pubspec.lock ./

# Get dependencies
RUN flutter pub get

# Copy source code
COPY frontend/ .

# Build web app
RUN flutter build web --release

# Production stage with Nginx
FROM nginx:alpine

# Copy built web app to nginx
COPY --from=build /app/build/web /usr/share/nginx/html

# Copy nginx configuration
COPY docker/frontend/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
