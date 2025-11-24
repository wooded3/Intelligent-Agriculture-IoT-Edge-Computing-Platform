import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

// 根据需要调整代理到你的 Spring Boot 后端，如 http://localhost:8080
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
});
