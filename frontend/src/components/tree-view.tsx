import { useState } from "react";
import type { TreeNode } from "@/lib/api";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { ChevronDown, ChevronRight, Plus, FolderTree } from "lucide-react";
import { cn } from "@/lib/utils";

interface Props {
  node: TreeNode;
  selectedId?: string;
  onSelect: (node: TreeNode) => void;
  onAddChild: (node: TreeNode) => void;
  depth?: number;
}

export function TreeView({ node, selectedId, onSelect, onAddChild, depth = 0 }: Props) {
  const [open, setOpen] = useState(true);
  const hasChildren = !!node.children && node.children.length > 0;
  const selected = selectedId === node.id;

  return (
    <div>
      <div
        className={cn(
          "group flex items-center gap-1 rounded-md px-2 py-1.5 hover:bg-accent cursor-pointer",
          selected && "bg-accent",
        )}
        style={{ paddingLeft: `${depth * 16 + 8}px` }}
        onClick={() => onSelect(node)}
      >
        <button
          onClick={(e) => {
            e.stopPropagation();
            setOpen((o) => !o);
          }}
          className="p-0.5 hover:bg-background rounded"
        >
          {hasChildren ? (
            open ? (
              <ChevronDown className="h-4 w-4" />
            ) : (
              <ChevronRight className="h-4 w-4" />
            )
          ) : (
            <span className="inline-block w-4" />
          )}
        </button>
        <FolderTree className="h-4 w-4 text-primary shrink-0" />
        <Badge variant="outline" className="font-mono text-xs">
          {node.code}
        </Badge>
        <span className="text-sm font-medium truncate">{node.name}</span>
        <Badge variant="secondary" className="text-xs ml-1">
          {node.type}
        </Badge>
        <Button
          size="sm"
          variant="ghost"
          className="ml-auto opacity-0 group-hover:opacity-100 h-7"
          onClick={(e) => {
            e.stopPropagation();
            onAddChild(node);
          }}
        >
          <Plus className="h-3 w-3" />
        </Button>
      </div>

      {open && hasChildren && (
        <div>
          {node.children!.map((child) => (
            <TreeView
              key={child.id}
              node={child}
              selectedId={selectedId}
              onSelect={onSelect}
              onAddChild={onAddChild}
              depth={depth + 1}
            />
          ))}
        </div>
      )}
    </div>
  );
}
