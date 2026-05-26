import { useEffect, useState } from "react";
import { ApiSettings, DEFAULT_SETTINGS, loadSettings, saveSettings } from "@/lib/api";

export function useSettings() {
  const [settings, setSettings] = useState<ApiSettings>(DEFAULT_SETTINGS);
  const [ready, setReady] = useState(false);

  useEffect(() => {
    setSettings(loadSettings());
    setReady(true);
  }, []);

  const update = (patch: Partial<ApiSettings>) => {
    setSettings((s) => {
      const next = { ...s, ...patch };
      saveSettings(next);
      return next;
    });
  };

  return { settings, update, ready };
}
