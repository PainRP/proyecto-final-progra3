import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import type { NodeRequest } from "@/lib/api";

interface Props {
  title: string;
  submitLabel: string;
  onSubmit: (data: NodeRequest) => Promise<void> | void;
  disabled?: boolean;
}

export function NodeForm({ title, submitLabel, onSubmit, disabled }: Props) {
  const [code, setCode] = useState("");
  const [name, setName] = useState("");
  const [type, setType] = useState("");
  const [description, setDescription] = useState("");
  const [submitting, setSubmitting] = useState(false);

  const handle = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!code || !name || !type) return;
    setSubmitting(true);
    try {
      await onSubmit({
        code,
        name,
        type,
        description: description || undefined,
      });
      setCode("");
      setName("");
      setType("");
      setDescription("");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <form onSubmit={handle} className="space-y-3">
      <h3 className="font-semibold text-sm">{title}</h3>
      <div className="grid grid-cols-2 gap-2">
        <div>
          <Label htmlFor="code" className="text-xs">Código</Label>
          <Input id="code" value={code} onChange={(e) => setCode(e.target.value)} placeholder="1.1.1" required />
        </div>
        <div>
          <Label htmlFor="type" className="text-xs">Tipo</Label>
          <Input id="type" value={type} onChange={(e) => setType(e.target.value)} placeholder="Activo" required />
        </div>
      </div>
      <div>
        <Label htmlFor="name" className="text-xs">Nombre</Label>
        <Input id="name" value={name} onChange={(e) => setName(e.target.value)} placeholder="Caja General" required />
      </div>
      <div>
        <Label htmlFor="description" className="text-xs">Descripción</Label>
        <Textarea
          id="description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="Opcional"
          rows={2}
        />
      </div>
      <Button type="submit" disabled={disabled || submitting} className="w-full">
        {submitting ? "Guardando…" : submitLabel}
      </Button>
    </form>
  );
}
