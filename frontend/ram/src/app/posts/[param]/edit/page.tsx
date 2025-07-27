"use client";

import PostEdit from "@/app/components/Post/PostEdit";
import { PostData } from "@/app/lib/definitions";
import { Button, Group, LoadingOverlay, Text } from "@mantine/core";
import { useForm } from "@mantine/form";

import { useParams, usePathname } from "next/navigation";
import router from "next/router";
import { useEffect, useState } from "react";

export default function editPost() {
  const params = useParams();
  const [postData, setPostData] = useState<PostData | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const originalTitleUrl = usePathname().replace("/edit", "");

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

  useEffect(() => {
    async function loadPost() {
      try {
        console.info(">>> params", params);

        const response = await fetch(`/api/posts/${params.param}`);
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

  const handleSubmit = async (values: typeof form.values) => {
    const originalTitle = originalTitleUrl.replace("/posts/", "");
    const patchUrl = `/api/posts/${originalTitle}`;

    const valuesToBeSubmitted = {
      ...values,
      originalTitle: decodeURI(originalTitle),
    };
    console.info("patching: " + patchUrl + ", with : " + valuesToBeSubmitted);
    try {
      const res = await fetch(patchUrl, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(valuesToBeSubmitted),
      });
      const data = await res.json();
      console.log(data);
      if (data.success) {
        router.push(originalTitleUrl);
      } else {
        console.error(data.error);
      }
    } catch (error) {
      console.error("API call failure");
    }
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
