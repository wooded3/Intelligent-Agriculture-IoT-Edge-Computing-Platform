/// <reference types="vite/client" />
/// <reference types="vitest/globals" />

import type { DefineComponent } from 'vue';

declare module '*.vue' {
  const component: DefineComponent<{}, {}, any>;
  export default component;
}

declare module '@element-plus/icons-vue' {
  import { DefineComponent } from 'vue';
  export const Plus: DefineComponent;
  export const Monitor: DefineComponent;
  export const CircleCheck: DefineComponent;
  export const Bell: DefineComponent;
  export const Warning: DefineComponent;
  export const DataAnalysis: DefineComponent;
  export const Setting: DefineComponent;
  export const Tools: DefineComponent;
  export const User: DefineComponent;
  const icons: Record<string, DefineComponent>;
  export default icons;
}