// API client for the Tree / Plan de Cuentas backend.
// Reads backend storage/strategy info; does not attempt to switch it from the UI.

export type Engine = "memory" | "postgres" | "mongo";
export type Strategy = "custom" | "collections";

export interface ApiSettings {
  baseUrl: string;
}

export interface ServerInfo {
  engine: Engine;
  strategy: Strategy;
}

const SETTINGS_KEY = "tree-api-settings";

export const DEFAULT_SETTINGS: ApiSettings = {
  baseUrl: "http://localhost:8081",
};

export function loadSettings(): ApiSettings {
  if (typeof window === "undefined") return DEFAULT_SETTINGS;
  try {
    const raw = localStorage.getItem(SETTINGS_KEY);
    if (!raw) return DEFAULT_SETTINGS;
    return { ...DEFAULT_SETTINGS, ...JSON.parse(raw) };
  } catch {
    return DEFAULT_SETTINGS;
  }
}

export function saveSettings(settings: ApiSettings) {
  localStorage.setItem(SETTINGS_KEY, JSON.stringify(settings));
}

// Schema types matching the OpenAPI contract
export interface NodeRequest {
  code: string;
  name: string;
  type: string;
  description?: string;
}

export interface TreeNodeData {
  id: string;
  code: string;
  name: string;
  type: string;
  description?: string;
  parentId?: string | null;
}

export interface TreeNode extends TreeNodeData {
  children?: TreeNode[];
}

export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
}

type RequestOptions = {
  allowNotFound?: boolean;
};

async function request<T>(
  settings: ApiSettings,
  path: string,
  init?: RequestInit,
  options?: RequestOptions,
): Promise<T> {
  const url = `${settings.baseUrl.replace(/\/$/, "")}${path}`;
  const res = await fetch(url, {
    ...init,
    headers: {
      "Content-Type": "application/json",
      ...(init?.headers ?? {}),
    },
  });

  if (options?.allowNotFound && res.status === 404) {
    return null as T;
  }

  const text = await res.text();
  let body: unknown = null;
  if (text) {
    try {
      body = JSON.parse(text);
    } catch {
      body = text;
    }
  }

  if (!res.ok) {
    const err = body as Partial<ErrorResponse> | string | null;
    const message =
      typeof err === "string"
        ? err
        : err?.message || `${res.status} ${res.statusText}`;
    throw new Error(message);
  }
  return body as T;
}

export const api = {
  createRoot: (s: ApiSettings, body: NodeRequest) =>
    request<TreeNodeData>(s, "/nodes/root", {
      method: "POST",
      body: JSON.stringify(body),
    }),
  addChild: (s: ApiSettings, parentId: string, body: NodeRequest) =>
    request<TreeNodeData>(s, `/nodes/${parentId}/children`, {
      method: "POST",
      body: JSON.stringify(body),
    }),
  getFullTree: (s: ApiSettings) =>
    request<TreeNode | null>(s, "/tree", undefined, { allowNotFound: true }),
  getSubtree: (s: ApiSettings, nodeId: string) =>
    request<TreeNode>(s, `/tree/${nodeId}`),
  getPath: (s: ApiSettings, nodeId: string) =>
    request<TreeNodeData[]>(s, `/nodes/${nodeId}/path`),
  getTraversal: (s: ApiSettings, type: "DFS" | "BFS") =>
    request<TreeNodeData[]>(s, `/tree/traversal?type=${type}`),
  getHeight: (s: ApiSettings) => request<{ height: number }>(s, "/tree/height"),
  getDepth: (s: ApiSettings, nodeId: string) =>
    request<{ depth: number }>(s, `/nodes/${nodeId}/depth`),
  getAncestors: (s: ApiSettings, nodeId: string) =>
    request<TreeNodeData[]>(s, `/nodes/${nodeId}/ancestors`),
  validateNoCycles: (s: ApiSettings) =>
    request<{ valid: boolean; message: string }>(s, "/tree/validate"),
  getServerInfo: (s: ApiSettings) => request<ServerInfo>(s, "/info"),
};
