"use client";

import {
  Group,
  InputWrapper,
  Skeleton,
  TextInput,
  useComputedColorScheme,
} from "@mantine/core";
import { UseFormReturnType } from "@mantine/form";
import dynamic from "next/dynamic";
import { useRef } from "react";
import ImageGallery, { UploadedImage } from "../ImageGalery/ImageGallery";
import type { TinyMCE } from "./TinyMCEEditor";

const TinyMCEEditor = dynamic(() => import("./TinyMCEEditor"), {
  ssr: false,
  loading: () => <Skeleton height={400} />,
});

interface PostEditTinyMCEProps {
  form: UseFormReturnType<{
    title: string;
    content: string;
    id: string;
  }>;
  error?: string | null;
}

export default function PostEditTinyMCE({ form, error }: PostEditTinyMCEProps) {
  const colorScheme = useComputedColorScheme("light");
  const editorRef = useRef<TinyMCE | null>(null);

  // Inserts at the cursor, or at the start if the editor was never focused.
  function insertImage({ name, url }: UploadedImage) {
    const editor = editorRef.current;
    if (!editor) return;
    editor.insertContent(
      editor.dom.createHTML("img", { src: url, alt: name }),
    );
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
      <br/>
      <InputWrapper
        label="Content:"
        labelElement="div"
        description=""
        id="post-editor-wrapper"
        error={error}
      >
        {/* TinyMCE can't swap skins in place, so remount it on theme change.
            getValues() works in both controlled and uncontrolled form modes. */}
        <TinyMCEEditor
          key={colorScheme}
          colorScheme={colorScheme}
          editorRef={editorRef}
          getInitialContent={() => form.getValues().content}
          onChange={(html) => form.setFieldValue("content", html)}
        />
      </InputWrapper>
      <Group justify="center" mt="md">
        <ImageGallery onUploadComplete={insertImage} />
      </Group>
    </>
  );
}
