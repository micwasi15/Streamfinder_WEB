#!/bin/sh

cat <<EOF > /usr/share/nginx/html/config.js
window.__APP_CONFIG__ = {
  API_URL: "${API_URL:-http://localhost:8080}"
};
EOF

exec "$@"
