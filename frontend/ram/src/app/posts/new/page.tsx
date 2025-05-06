"use client";

import AuthGuard from "@/app/components/AuthGuard";
import PostEdit from "@/app/components/PostEdit";
import { Button, Group, SimpleGrid, Text, TextInput } from "@mantine/core";
import { useForm } from "@mantine/form";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function NewPostPage() {
  console.log(">>> rendering NewPostPage");
  const router = useRouter();

  const title: string = "";
  const content: string = "";

  const form = useForm({
    mode: "uncontrolled",
    initialValues: {
      content: content,
      title: title,
    },
    validate: {
      title: (value) => (value.trim() === "" ? "Title is required" : null),
      content: (value) => (value.trim() === "" ? "Content is required" : null),
    },
    validateInputOnChange: true,
  });

  const handleSubmit = async (values: typeof form.values) => {
    console.log(values);
    try {
      const res = await fetch("/api/posts", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(values),
      });
      const data = await res.json();
      console.log(data);
      if (data.success) {
        console.log(">>> redirecting to /posts");
        router.push("/posts");
      } else {
        console.error(data.error);
      }
    } catch (error) {
      console.error(error);
    }
  };

  const [contentError, setContentError] = useState<string | null>(null);

  useEffect(() => {
    console.log(">>> useEffect:", contentError);
    setContentError(form.errors.content as string | null);
  }, [form.values.content, form.errors.content]);

  return (
    <AuthGuard>
      <form onSubmit={form.onSubmit(handleSubmit)}>
        <SimpleGrid cols={1} spacing={2} verticalSpacing={"lg"}>
          <PostEdit form={form} error={contentError}></PostEdit>

          <Group justify="flex-end" mt="md">
            <Button type="submit">Submit</Button>
          </Group>
        </SimpleGrid>
      </form>
    </AuthGuard>
  );
}
