"use client";

// Self-hosted TinyMCE bundle. TinyMCE touches `window` on import, so this module
// must only be loaded in the browser (see the dynamic import in PostEditTinyMCE).

// The core has to come first: everything below registers itself on it.
import "tinymce/tinymce";

import "tinymce/icons/default/icons";
import "tinymce/models/dom/model";
import "tinymce/themes/silver/theme";

import "tinymce/plugins/autolink";
import "tinymce/plugins/autoresize";
import "tinymce/plugins/code";
import "tinymce/plugins/emoticons";
// The emoji list, which the plugin would otherwise fetch at runtime.
import "tinymce/plugins/emoticons/js/emojis.js";
import "tinymce/plugins/image";
import "tinymce/plugins/link";
import "tinymce/plugins/lists";
import "tinymce/plugins/table";

// Skins for both color schemes, bundled so nothing is fetched at runtime.
import "tinymce/skins/content/dark/content.js";
import "tinymce/skins/content/default/content.js";
import "tinymce/skins/ui/oxide-dark/content.js";
import "tinymce/skins/ui/oxide-dark/skin.js";
import "tinymce/skins/ui/oxide/content.js";
import "tinymce/skins/ui/oxide/skin.js";

import { Editor, IAllProps } from "@tinymce/tinymce-react";
import { RefObject, useState } from "react";

type InitOptions = NonNullable<IAllProps["init"]>;
export type TinyMCE = Parameters<NonNullable<InitOptions["setup"]>>[0];

interface TinyMCEEditorProps {
  /** Read once, on mount. TinyMCE owns the content from then on. */
  getInitialContent: () => string;
  onChange: (html: string) => void;
  colorScheme: "light" | "dark";
  /** Set to the editor instance while it is ready, null otherwise. */
  editorRef?: RefObject<TinyMCE | null>;
}

// Keep in sync with the AppShell header height in AriesLayout.
const HEADER_HEIGHT = 64;

// Offered for text and table cells.
const COLORS = [
  "#25262b",
  "#868e96",
  "#fa5252",
  "#e64980",
  "#be4bdb",
  "#7950f2",
  "#4c6ef5",
  "#228be6",
  "#15aabf",
  "#12b886",
  "#40c057",
  "#82c91e",
  "#fab005",
  "#fd7e14",
];

const TOOLBAR = [
  "code",
  "bold italic underline strikethrough removeformat highlight",
  "h1 h2 h3 h4",
  "blockquote hr bullist numlist subscript superscript codeblock",
  "link unlink",
  "alignleft aligncenter alignjustify alignright",
  "undo redo",
  "forecolor image table emoticons",
].join(" | ");

function addFormatToggle(
  editor: TinyMCE,
  name: string,
  { icon, tooltip, format }: { icon: string; tooltip: string; format: string },
) {
  editor.ui.registry.addToggleButton(name, {
    icon,
    tooltip,
    onAction: () => editor.execCommand("mceToggleFormat", false, format),
    onSetup: (api) => {
      let unbind: (() => void) | undefined;
      const watch = () => {
        api.setActive(editor.formatter.match(format));
        unbind = editor.formatter.formatChanged(format, (active) =>
          api.setActive(active),
        ).unbind;
      };

      // The toolbar is rendered before the formatter exists.
      if (editor.initialized) {
        watch();
      } else {
        editor.on("init", watch);
      }

      return () => {
        editor.off("init", watch);
        unbind?.();
      };
    },
  });
}

export default function TinyMCEEditor({
  getInitialContent,
  onChange,
  colorScheme,
  editorRef,
}: TinyMCEEditorProps) {
  // Must stay stable: the wrapper resets the editor when `initialValue` changes.
  const [initialContent] = useState(getInitialContent);
  const isDark = colorScheme === "dark";

  return (
    <Editor
      licenseKey="gpl"
      initialValue={initialContent}
      onEditorChange={onChange}
      onInit={(_event, editor) => {
        if (!editorRef) return;
        editorRef.current = editor;
        editor.on("remove", () => {
          if (editorRef.current === editor) editorRef.current = null;
        });
      }}
      init={{
        skin: isDark ? "oxide-dark" : "oxide",
        content_css: isDark ? "dark" : "default",
        content_style: `
          pre {
            background-color: ${isDark ? "#1f1f1f" : "#f8f9fa"};
            color: ${isDark ? "#b8b8b8" : "#212529"};
            padding: 12px 16px;
            border-radius: 4px;
            overflow-x: auto;
          }
          pre code {
            background: none;
            padding: 0;
          }
          /* Same defaults as published tables. */
          table {
            width: 100%;
          }
          th {
            text-align: left;
          }
          img {
            max-width: 100%;
          }
        `,
        plugins: "autolink autoresize code emoticons image link lists table",
        toolbar: TOOLBAR,
        toolbar_mode: "wrap",
        toolbar_sticky: true,
        toolbar_sticky_offset: HEADER_HEIGHT,
        menubar: false,
        promotion: false,
        min_height: 400,
        iframe_aria_text: "Post content",
        color_map: COLORS.flatMap((color) => [color, color]),

        // Semantic tags instead of styled spans.
        formats: {
          underline: { inline: "u", exact: true },
          highlight: { inline: "mark" },
        },
        table_toolbar:
          "tableprops tabledelete | tablerowheader tablecolheader | " +
          "tableinsertrowbefore tableinsertrowafter tabledeleterow | " +
          "tableinsertcolbefore tableinsertcolafter tabledeletecol | " +
          "tablemergecells tablesplitcells | tablecellbackgroundcolor",
        table_header_type: "sectionCells",
        // sanitizePostContent drops base64 sources when the post is displayed.
        paste_data_images: false,
        // Leave URLs exactly as typed instead of making them page-relative.
        convert_urls: false,
        entity_encoding: "raw",

        setup: (editor) => {
          addFormatToggle(editor, "highlight", {
            icon: "highlight-bg-color",
            tooltip: "Highlight",
            format: "highlight",
          });
          addFormatToggle(editor, "codeblock", {
            icon: "code-sample",
            tooltip: "Code block",
            format: "pre",
          });
        },
      }}
    />
  );
}
