import { ApiSettings, ServerInfo } from "@/lib/api";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Badge } from "@/components/ui/badge";
import { Database, GitBranch, Server } from "lucide-react";

interface Props {
  settings: ApiSettings;
  serverInfo: ServerInfo | null;
  onChange: (patch: Partial<ApiSettings>) => void;
}

export function SettingsBar({ settings, serverInfo, onChange }: Props) {
  return (
    <div className="border-b bg-card">
      <div className="container mx-auto px-4 py-3 flex flex-wrap items-end gap-4">
        <div className="flex items-center gap-2 mr-2">
          <GitBranch className="h-5 w-5 text-primary" />
          <span className="font-semibold">Plan de Cuentas</span>
          <Badge variant="secondary" className="ml-1">
            {serverInfo ? `${serverInfo.engine} · ${serverInfo.strategy}` : "detectando"}
          </Badge>
        </div>

        <div className="flex flex-col gap-1 min-w-[260px] flex-1">
          <Label htmlFor="baseUrl" className="text-xs flex items-center gap-1">
            <Server className="h-3 w-3" /> Backend URL
          </Label>
          <Input
            id="baseUrl"
            value={settings.baseUrl}
            onChange={(e) => onChange({ baseUrl: e.target.value })}
            placeholder="http://localhost:8081"
          />
        </div>

        <div className="flex flex-col gap-1 min-w-[160px]">
          <Label className="text-xs flex items-center gap-1">
            <Database className="h-3 w-3" /> Motor
          </Label>
          <Badge variant="outline" className="w-fit">
            {serverInfo?.engine ?? "desconocido"}
          </Badge>
        </div>

        <div className="flex flex-col gap-1 min-w-[160px]">
          <Label className="text-xs">Estrategia</Label>
          <Badge variant="outline" className="w-fit">
            {serverInfo?.strategy ?? "desconocida"}
          </Badge>
        </div>
      </div>
    </div>
  );
}
