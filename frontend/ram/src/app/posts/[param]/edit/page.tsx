"use client";

import AuthGuard from "@/app/components/AuthGuard";
import PostEdit from "@/app/components/Post/PostEdit";
import { PostData } from "@/app/lib/definitions";
import { Button, Group, LoadingOverlay, Text } from "@mantine/core";
import { useForm } from "@mantine/form";

import { useParams, usePathname, useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function editPost() {
  const params = useParams();
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const originalTitleUrl = usePathname().replace("/edit", "");
  const router = useRouter();

  const form = useForm({
    mode: "controlled",
    initialValues: {
      title: "",
      content: "",
      id: "",
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
        const response = await fetch(`/api/posts/${params.param}`);
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data: PostData = await response.json();
        form.setValues({
          title: data.title.trim(),
          content: data.content.trim(),
          id: data.id,
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
    setIsSubmitting(true); // start submitting

    const originalTitle = originalTitleUrl.replace("/posts/", "");
    const patchUrl = `/api/posts/${originalTitle}`;

    const valuesToBeSubmitted = {
      ...values,
      title: values.title.trim(),
      content: values.content.trim(),
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
      if (res.ok) {
        const newTitle = encodeURI(data.postDto[0].titleUrl);
        router.push(`/posts/${newTitle}`);
      } else {
        console.error(data.error);
      }
    } catch (error) {
      console.error("API call failure");
    } finally {
      setIsSubmitting(false);
    }
  };

  if (isLoading) {
    return (
      <LoadingOverlay
        zIndex={1000}
        overlayProps={{ radius: "sm", blur: 2 }}
        visible={isSubmitting}
      />
    );
  }

  if (error) {
    return <Text c="red">Error: {error}</Text>;
  }

  return (
    <AuthGuard>
      <form onSubmit={form.onSubmit(handleSubmit)}>
        {/* show overlay while submitting */}
        <LoadingOverlay
          zIndex={1000}
          overlayProps={{ radius: "sm", blur: 2 }}
          visible={isSubmitting}
        />
        <PostEdit form={form}></PostEdit>
        <Group justify="flex-end" mt="md">
          <Button type="submit">Submit</Button>
        </Group>
      </form>
    </AuthGuard>
  );
}
