"use client";

import PostEdit from "@/app/components/PostEdit";
import { PostData } from "@/app/lib/definitions";
import { Button, Group, LoadingOverlay, Text } from "@mantine/core";
import { useForm } from "@mantine/form";

import { useParams } from "next/navigation";
import { useEffect, useState } from "react";

export default function editPost() {
  const params = useParams();
  const [postData, setPostData] = useState<PostData | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const form = useForm({
    mode: "controlled", //
    initialValues: {
      title: "",
      content: "",
    },
    validate: {
      title: (value) =>
        value.length < 2 ? "Must be longer than two characters" : null,
      content: (value) =>
        value.length < 3 ? "First name must have at least 3 characters" : null,
    },
  });

  //debug
  // useEffect(() => {
  //   console.log("Current form values:", form.values);
  // }, [form.values]);

  useEffect(() => {
    async function loadPost() {
      try {
        console.info(">>> params", params);
        console.info(">>> fetching post data for edit: ", params.id);

        const response = await fetch(`/api/posts/${params.id}`);
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data: PostData = await response.json();
        console.log("Fetched post data:", data);
        setPostData(data);
        form.setValues({
          title: data.title,
          content: data.content,
        });
      } catch (error) {
        setError(
          error instanceof Error ? error.message : "An unknown error occurred"
        );

        console.error("Error loading post:", error);
      } finally {
        setIsLoading(false);
      }
    }

    loadPost();
  }, [params.id]);

  const handleSubmit = (values: typeof form.values) => {
    console.log(values);
  };

  if (isLoading) {
    return (
      <LoadingOverlay
        zIndex={1000}
        overlayProps={{ radius: "sm", blur: 2 }}
        visible={true}
      />
    );
  }

  if (error) {
    return <Text c="red">Error: {error}</Text>;
  }

  return (
    <form onSubmit={form.onSubmit(handleSubmit)}>
      <PostEdit form={form}></PostEdit>
      <Group justify="flex-end" mt="md">
        <Button type="submit">Submit</Button>
      </Group>
    </form>
  );
}
