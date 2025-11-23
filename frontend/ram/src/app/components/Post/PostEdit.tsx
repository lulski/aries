"use client";

import "@mantine/tiptap/styles.css";
import "./styles.scss";

import { Button, InputWrapper, TextInput } from "@mantine/core";
import { UseFormReturnType } from "@mantine/form";
import { RichTextEditor } from "@mantine/tiptap";
import { Color } from "@tiptap/extension-color";
// import Document from "@tiptap/extension-document";
// import { Dropcursor } from "@tiptap/extension-dropcursor";
import Highlight from "@tiptap/extension-highlight";
import Image from "@tiptap/extension-image";
// import Paragraph from "@tiptap/extension-paragraph";
import SubScript from "@tiptap/extension-subscript";
import Superscript from "@tiptap/extension-superscript";
// import Text from "@tiptap/extension-text";
import TextAlign from "@tiptap/extension-text-align";
import { TextStyle } from "@tiptap/extension-text-style";
import { useEditor } from "@tiptap/react";
import StarterKit from "@tiptap/starter-kit";

interface PostEditProps {
  form: UseFormReturnType<{
    title: string;
    content: string;
    id: string;
  }>;
  error?: string | null;
}

export default function PostEdit({ form, error }: PostEditProps) {
  const editor = useEditor({
    extensions: [
      StarterKit,
      TextStyle,
      Color,
      // Link,
      Superscript,
      SubScript,
      Highlight,
      TextAlign.configure({ types: ["heading", "paragraph"] }),
      Image.configure({
        inline: true,
        resize: {
          enabled: true,
          directions: ["top", "bottom", "left", "right"],
          minWidth: 50,
          minHeight: 50,
          alwaysPreserveAspectRatio: true,
        },
      }),
      // Document,
      // Paragraph,
      // Text,
      // Dropcursor,
    ],
    content: form.values.content,
    onUpdate: ({ editor }) => form.setFieldValue("content", editor.getHTML()),
    immediatelyRender: false,
    shouldRerenderOnTransaction: true,
  });

  function addImage(editor: ReturnType<typeof useEditor> | null) {
    const url = window.prompt("Image URL");
    if (!url) return;

    // guard: ensure editor is initialized and has .chain()
    if (!editor || typeof (editor as any).chain !== "function") {
      console.error("editor not ready or not a TipTap Editor instance", editor);
      return;
    }

    // safe call
    //editor.chain().focus().setImage({ src: url }).run();
    editor.commands.setImage({ src: url });

    // keep form/content in sync if you track it
    // form.setFieldValue("content", editor.getHTML());
  }

  return (
    <>
      <input type="hidden" name="id" {...form.getInputProps("id")} />
      <TextInput
        withAsterisk
        label="Title:"
        placeholder=""
        {...form.getInputProps("title")}
      />
      <InputWrapper
        label="Content:"
        description=""
        id="post-editor-wrapper"
        error={error}
      >
        <RichTextEditor
          id="post-editor"
          editor={editor}
          styles={{ content: { minHeight: 400 } }}
        >
          <RichTextEditor.Toolbar
            sticky
            stickyOffset="var(--docs-header-height)"
          >
            <RichTextEditor.SourceCode />
            <RichTextEditor.ControlsGroup>
              <RichTextEditor.Bold />
              <RichTextEditor.Italic />
              <RichTextEditor.Underline />
              <RichTextEditor.Strikethrough />
              <RichTextEditor.ClearFormatting />
              <RichTextEditor.Highlight />
            </RichTextEditor.ControlsGroup>
            <RichTextEditor.ControlsGroup>
              <RichTextEditor.H1 />
              <RichTextEditor.H2 />
              <RichTextEditor.H3 />
              <RichTextEditor.H4 />
            </RichTextEditor.ControlsGroup>
            <RichTextEditor.ControlsGroup>
              <RichTextEditor.Blockquote />
              <RichTextEditor.Hr />
              <RichTextEditor.BulletList />
              <RichTextEditor.OrderedList />
              <RichTextEditor.Subscript />
              <RichTextEditor.Superscript />
              <RichTextEditor.CodeBlock />
            </RichTextEditor.ControlsGroup>
            <RichTextEditor.ControlsGroup>
              <RichTextEditor.Link />
              <RichTextEditor.Unlink />
            </RichTextEditor.ControlsGroup>
            <RichTextEditor.ControlsGroup>
              <RichTextEditor.AlignLeft />
              <RichTextEditor.AlignCenter />
              <RichTextEditor.AlignJustify />
              <RichTextEditor.AlignRight />
            </RichTextEditor.ControlsGroup>
            <RichTextEditor.ControlsGroup>
              <RichTextEditor.Undo />
              <RichTextEditor.Redo />
            </RichTextEditor.ControlsGroup>
            <RichTextEditor.ColorPicker
              colors={[
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
              ]}
            />

            {/* <RichTextEditor.ControlsGroup>
              <RichTextEditor.Color color="#F03E3E" />
              <RichTextEditor.Color color="#7048E8" />
              <RichTextEditor.Color color="#1098AD" />
              <RichTextEditor.Color color="#37B24D" />
              <RichTextEditor.Color color="#F59F00" />

              <RichTextEditor.UnsetColor />
            </RichTextEditor.ControlsGroup> */}
          </RichTextEditor.Toolbar>

          <RichTextEditor.Content />
        </RichTextEditor>
      </InputWrapper>
      <Button onClick={() => addImage(editor)}>Set image</Button>
      {/* <button type="button" onClick={() => addImage(editor)}>
        Set image
      </button> */}
    </>
  );
}
