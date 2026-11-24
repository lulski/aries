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
import ImageGallery from "../ImageGalery/ImageGallery";

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
          getInitialContent={() => form.getValues().content}
          onChange={(html) => form.setFieldValue("content", html)}
        />
      </InputWrapper>
      <Group justify="center" mt="md">
        <ImageGallery />
      </Group>
    </>
  );
}
