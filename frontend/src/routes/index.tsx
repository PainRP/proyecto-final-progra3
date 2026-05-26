import { createFileRoute } from "@tanstack/react-router";
import { useEffect, useState } from "react";
import { Toaster, toast } from "sonner";

import { useSettings } from "@/hooks/use-settings";
import { api, type NodeRequest, type TreeNode, type TreeNodeData, type ServerInfo } from "@/lib/api";
import { SettingsBar } from "@/components/settings-bar";
import { NodeForm } from "@/components/node-form";
import { TreeView } from "@/components/tree-view";

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Separator } from "@/components/ui/separator";
import {
  RefreshCw,
  Ruler,
  Route as RouteIcon,
  ListTree,
  ShieldCheck,
  ArrowUpFromLine,
  Layers,
} from "lucide-react";

export const Route = createFileRoute("/")({
  component: Index,
});

type ResultPayload =
  | { kind: "list"; title: string; data: TreeNodeData[] }
  | { kind: "value"; title: string; value: string }
  | { kind: "validation"; title: string; valid: boolean; message: string }
  | { kind: "subtree"; title: string; node: TreeNode };

function Index() {
  const { settings, update, ready } = useSettings();
  const [tree, setTree] = useState<TreeNode | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [selected, setSelected] = useState<TreeNodeData | null>(null);
  const [addChildOf, setAddChildOf] = useState<TreeNodeData | null>(null);
  const [result, setResult] = useState<ResultPayload | null>(null);
  const [serverInfo, setServerInfo] = useState<ServerInfo | null>(null);

  const refresh = async () => {
    setLoading(true);
    setError(null);
    try {
      const t = await api.getFullTree(settings);
      if (!t) {
        setTree(null);
        return;
      }
      setTree(t);
    } catch (e) {
      const msg = e instanceof Error ? e.message : "Error desconocido";
      setError(msg);
      setTree(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!ready) return;
    refresh();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [ready, settings.baseUrl]);

  useEffect(() => {
    if (!ready) return;
    let active = true;
    api
      .getServerInfo(settings)
      .then((info) => {
        if (active) setServerInfo(info);
      })
      .catch(() => {
        if (active) setServerInfo(null);
      });
    return () => {
      active = false;
    };
  }, [ready, settings.baseUrl]);

  const handleCreateRoot = async (data: NodeRequest) => {
    try {
      await api.createRoot(settings, data);
      toast.success("Cuenta raíz creada");
      refresh();
    } catch (e) {
      toast.error(e instanceof Error ? e.message : "Error");
    }
  };

  const handleAddChild = async (data: NodeRequest) => {
    if (!addChildOf) return;
    try {
      await api.addChild(settings, addChildOf.id, data);
      toast.success(`Subcuenta agregada a ${addChildOf.code}`);
      setAddChildOf(null);
      refresh();
    } catch (e) {
      toast.error(e instanceof Error ? e.message : "Error");
    }
  };

  const run = async (fn: () => Promise<ResultPayload>) => {
    try {
      const r = await fn();
      setResult(r);
    } catch (e) {
      toast.error(e instanceof Error ? e.message : "Error");
    }
  };

  if (!ready) return null;

  return (
    <div className="min-h-screen bg-background">
      <SettingsBar settings={settings} serverInfo={serverInfo} onChange={update} />
      <Toaster position="top-right" richColors />

      <main className="container mx-auto px-4 py-6 grid gap-6 lg:grid-cols-[1fr_360px]">
        {/* TREE */}
        <Card className="min-h-[500px]">
          <CardHeader className="flex flex-row items-center justify-between">
            <div>
              <CardTitle className="flex items-center gap-2">
                <ListTree className="h-5 w-5" />
                Árbol del plan de cuentas
              </CardTitle>
              <p className="text-sm text-muted-foreground mt-1">
                Motor: <Badge variant="outline">{serverInfo?.engine ?? "desconocido"}</Badge>{" "}
                · Estrategia: <Badge variant="outline">{serverInfo?.strategy ?? "desconocida"}</Badge>
              </p>
            </div>
            <Button variant="outline" size="sm" onClick={refresh} disabled={loading}>
              <RefreshCw className={`h-4 w-4 mr-1 ${loading ? "animate-spin" : ""}`} />
              Refrescar
            </Button>
          </CardHeader>
          <CardContent>
            {error && (
              <div className="rounded-md border border-destructive/50 bg-destructive/10 p-4 text-sm">
                <p className="font-medium text-destructive">No se pudo cargar el árbol</p>
                <p className="text-muted-foreground mt-1">{error}</p>
                <p className="text-xs text-muted-foreground mt-2">
                  Verifica que el backend esté corriendo en{" "}
                  <code className="font-mono">{settings.baseUrl}</code>, o crea la cuenta
                  raíz en el panel derecho.
                </p>
              </div>
            )}
            {!error && !tree && !loading && (
              <div className="rounded-md border border-dashed p-8 text-center text-sm text-muted-foreground">
                Sin datos. Crea la cuenta raíz para comenzar.
              </div>
            )}
            {tree && (
              <ScrollArea className="h-[500px] pr-2">
                <TreeView
                  node={tree}
                  selectedId={selected?.id}
                  onSelect={(n) => setSelected(n)}
                  onAddChild={(n) => setAddChildOf(n)}
                />
              </ScrollArea>
            )}
          </CardContent>
        </Card>

        {/* SIDE PANEL */}
        <div className="space-y-4">
          <Card>
            <CardContent className="pt-6">
              <Tabs defaultValue={tree ? "child" : "root"}>
                <TabsList className="w-full grid grid-cols-2">
                  <TabsTrigger value="root" disabled={!!tree}>
                    Crear raíz
                  </TabsTrigger>
                  <TabsTrigger value="child" disabled={!selected}>
                    Agregar hija
                  </TabsTrigger>
                </TabsList>
                <TabsContent value="root" className="pt-4">
                  <NodeForm
                    title="Inicializar plan de cuentas"
                    submitLabel="Crear raíz"
                    onSubmit={handleCreateRoot}
                    disabled={!!tree}
                  />
                </TabsContent>
                <TabsContent value="child" className="pt-4">
                  {selected ? (
                    <NodeForm
                      title={`Agregar hija de "${selected.code} ${selected.name}"`}
                      submitLabel="Agregar"
                      onSubmit={async (data) => {
                        try {
                          await api.addChild(settings, selected.id, data);
                          toast.success("Subcuenta agregada");
                          refresh();
                        } catch (e) {
                          toast.error(e instanceof Error ? e.message : "Error");
                        }
                      }}
                    />
                  ) : (
                    <p className="text-sm text-muted-foreground">
                      Selecciona un nodo del árbol.
                    </p>
                  )}
                </TabsContent>
              </Tabs>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="pb-3">
              <CardTitle className="text-base">Operaciones del árbol</CardTitle>
            </CardHeader>
            <CardContent className="space-y-2">
              <Button
                variant="outline"
                className="w-full justify-start"
                onClick={() =>
                  run(async () => ({
                    kind: "value",
                    title: "Altura del árbol",
                    value: String((await api.getHeight(settings)).height),
                  }))
                }
              >
                <Ruler className="h-4 w-4 mr-2" /> Altura
              </Button>

              <Button
                variant="outline"
                className="w-full justify-start"
                onClick={() =>
                  run(async () => ({
                    kind: "list",
                    title: "Recorrido DFS",
                    data: await api.getTraversal(settings, "DFS"),
                  }))
                }
              >
                <Layers className="h-4 w-4 mr-2" /> Recorrido DFS
              </Button>

              <Button
                variant="outline"
                className="w-full justify-start"
                onClick={() =>
                  run(async () => ({
                    kind: "list",
                    title: "Recorrido BFS",
                    data: await api.getTraversal(settings, "BFS"),
                  }))
                }
              >
                <Layers className="h-4 w-4 mr-2" /> Recorrido BFS
              </Button>

              <Button
                variant="outline"
                className="w-full justify-start"
                onClick={() =>
                  run(async () => {
                    const r = await api.validateNoCycles(settings);
                    return {
                      kind: "validation",
                      title: "Validación de ciclos",
                      valid: r.valid,
                      message: r.message,
                    };
                  })
                }
              >
                <ShieldCheck className="h-4 w-4 mr-2" /> Validar ciclos
              </Button>

              <Separator className="my-2" />
              <p className="text-xs text-muted-foreground">
                {selected ? (
                  <>Operaciones sobre <span className="font-mono">{selected.code}</span></>
                ) : (
                  "Selecciona un nodo para habilitar más operaciones."
                )}
              </p>

              <Button
                variant="outline"
                className="w-full justify-start"
                disabled={!selected}
                onClick={() =>
                  selected &&
                  run(async () => ({
                    kind: "value",
                    title: `Profundidad de ${selected.code}`,
                    value: String((await api.getDepth(settings, selected.id)).depth),
                  }))
                }
              >
                <Ruler className="h-4 w-4 mr-2" /> Profundidad
              </Button>

              <Button
                variant="outline"
                className="w-full justify-start"
                disabled={!selected}
                onClick={() =>
                  selected &&
                  run(async () => ({
                    kind: "list",
                    title: `Ruta a ${selected.code}`,
                    data: await api.getPath(settings, selected.id),
                  }))
                }
              >
                <RouteIcon className="h-4 w-4 mr-2" /> Ruta desde raíz
              </Button>

              <Button
                variant="outline"
                className="w-full justify-start"
                disabled={!selected}
                onClick={() =>
                  selected &&
                  run(async () => ({
                    kind: "list",
                    title: `Ancestros de ${selected.code}`,
                    data: await api.getAncestors(settings, selected.id),
                  }))
                }
              >
                <ArrowUpFromLine className="h-4 w-4 mr-2" /> Ancestros
              </Button>

              <Button
                variant="outline"
                className="w-full justify-start"
                disabled={!selected}
                onClick={() =>
                  selected &&
                  run(async () => ({
                    kind: "subtree",
                    title: `Subárbol de ${selected.code}`,
                    node: await api.getSubtree(settings, selected.id),
                  }))
                }
              >
                <ListTree className="h-4 w-4 mr-2" /> Subárbol
              </Button>
            </CardContent>
          </Card>
        </div>
      </main>

      {/* Add child dialog (triggered from tree hover button) */}
      <Dialog open={!!addChildOf} onOpenChange={(o) => !o && setAddChildOf(null)}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>
              Agregar hija de {addChildOf?.code} — {addChildOf?.name}
            </DialogTitle>
          </DialogHeader>
          <NodeForm
            title=""
            submitLabel="Agregar subcuenta"
            onSubmit={handleAddChild}
          />
        </DialogContent>
      </Dialog>

      {/* Result dialog */}
      <Dialog open={!!result} onOpenChange={(o) => !o && setResult(null)}>
        <DialogContent className="max-w-2xl">
          <DialogHeader>
            <DialogTitle>{result?.title}</DialogTitle>
          </DialogHeader>
          <ResultView result={result} />
        </DialogContent>
      </Dialog>
    </div>
  );
}

function ResultView({ result }: { result: ResultPayload | null }) {
  if (!result) return null;
  if (result.kind === "value") {
    return <p className="text-4xl font-bold text-center py-6">{result.value}</p>;
  }
  if (result.kind === "validation") {
    return (
      <div className="space-y-3 py-2">
        <Badge variant={result.valid ? "default" : "destructive"} className="text-sm">
          {result.valid ? "Válido" : "Inválido"}
        </Badge>
        <p className="text-sm text-muted-foreground">{result.message}</p>
      </div>
    );
  }
  if (result.kind === "list") {
    return (
      <ScrollArea className="max-h-[60vh]">
        <div className="space-y-1">
          {result.data.length === 0 && (
            <p className="text-sm text-muted-foreground">Sin resultados.</p>
          )}
          {result.data.map((n, i) => (
            <div
              key={n.id}
              className="flex items-center gap-2 rounded-md border p-2 text-sm"
            >
              <span className="text-xs text-muted-foreground w-6">{i + 1}.</span>
              <Badge variant="outline" className="font-mono text-xs">{n.code}</Badge>
              <span className="font-medium">{n.name}</span>
              <Badge variant="secondary" className="text-xs ml-auto">{n.type}</Badge>
            </div>
          ))}
        </div>
      </ScrollArea>
    );
  }
  if (result.kind === "subtree") {
    return (
      <ScrollArea className="max-h-[60vh]">
        <pre className="text-xs bg-muted p-3 rounded-md">
          {JSON.stringify(result.node, null, 2)}
        </pre>
      </ScrollArea>
    );
  }
  return null;
}
