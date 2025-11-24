<template>
  <div class="app">
    <header class="app-header">
      <h1>IAIoTECP 智慧农业物联网平台</h1>
      <p>前端（Vue 3 + TypeScript）基础结构</p>
    </header>

    <main class="app-main">
      <section class="card">
        <h2>当前状态</h2>
        <p>前端已搭建完成，可以开始开发业务页面。</p>
        <ul>
          <li>技术栈：Vue 3 + TypeScript + Vite</li>
          <li>目录：<code>src/components</code>、<code>src/views</code> 等可自行扩展</li>
          <li>后端代理：使用 <code>/api</code> 代理到 Spring Boot</li>
        </ul>
      </section>

      <section class="card">
        <h2>示例接口调用（占位）</h2>
        <p>后续可以在这里调用后端接口，例如：</p>
        <pre><code>GET /api/devices
GET /api/data
        </code></pre>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
// 目前只做静态页面展示，后续可引入 axios/fetch 调用后端
</script>
