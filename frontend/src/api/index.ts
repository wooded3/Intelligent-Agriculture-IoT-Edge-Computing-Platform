const API_BASE = '/api';

export interface ApiResponse<T> {
  code: number;
  msg: string;
  data: T;
}

async function request<T>(url: string, options?: RequestInit): Promise<ApiResponse<T>> {
  const res = await fetch(`${API_BASE}${url}`, {
    headers: {
      'Content-Type': 'application/json',
      ...options?.headers
    },
    ...options
  });

  if (!res.ok) {
    throw new Error(`HTTP ${res.status}: ${res.statusText}`);
  }

  const body = await res.json();
  if (body.code !== 1) {
    throw new Error(body.msg || '请求失败');
  }

  return body;
}

// 设备管理
export const deviceApi = {
  list: (params?: { page?: number; pageSize?: number; classroomId?: string; type?: string }) => {
    const query = new URLSearchParams();
    if (params?.page) query.append('page', params.page.toString());
    if (params?.pageSize) query.append('pageSize', params.pageSize.toString());
    if (params?.classroomId) query.append('classroomId', params.classroomId);
    if (params?.type) query.append('type', params.type);
    return request<{ total: number; rows: any[] }>(`/devices?${query}`);
  },
  getById: (id: string) => request<any>(`/devices/${id}`),
  create: (data: any) => request<any>('/devices', { method: 'POST', body: JSON.stringify(data) }),
  update: (id: string, data: any) => request<any>(`/devices/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  delete: (id: string) => request<void>(`/devices/${id}`, { method: 'DELETE' }),
  updateStatus: (id: string, status: string) => request<void>(`/devices/${id}/status?status=${status}`, { method: 'PATCH' }),
  getStats: () => request<{ total: number; online: number; offline: number; error: number }>('/devices/stats')
};

// 数据查询
export const dataApi = {
  list: (params?: { deviceId?: string; startTime?: string; endTime?: string; page?: number; pageSize?: number }) => {
    const query = new URLSearchParams();
    if (params?.deviceId) query.append('deviceId', params.deviceId);
    if (params?.startTime) query.append('startTime', params.startTime);
    if (params?.endTime) query.append('endTime', params.endTime);
    if (params?.page) query.append('page', params.page.toString());
    if (params?.pageSize) query.append('pageSize', params.pageSize.toString());
    return request<{ total: number; rows: any[] }>(`/sensor-data?${query}`);
  },
  create: (data: any) => request<void>('/sensor-data', { method: 'POST', body: JSON.stringify(data) })
};

// 告警管理
export const alertApi = {
  getCurrent: () => request<any[]>('/alerts/current'),
  getRules: () => request<any[]>('/alerts/rules'),
  createRule: (data: any) => request<any>('/alerts/rules', { method: 'POST', body: JSON.stringify(data) }),
  resolveAlert: (alertId: string) => request<void>(`/alerts/${alertId}/resolve`, { method: 'PATCH' })
};

// 自动化控制
export const automationApi = {
  getRules: () => request<any[]>('/automation/rules'),
  createRule: (data: any) => request<any>('/automation/rules', { method: 'POST', body: JSON.stringify(data) }),
  toggleRule: (id: string) => request<void>(`/automation/rules/${id}/toggle`, { method: 'PATCH' }),
  deleteRule: (id: string) => request<void>(`/automation/rules/${id}`, { method: 'DELETE' })
};

// 系统管理
export const systemApi = {
  getClassrooms: () => request<any[]>('/classrooms')
};

