"use client";

import AuthGuard from "@/app/components/AuthGuard";
import PostEdit from "@/app/components/PostEdit";
import { Button, Group, SimpleGrid, Text, TextInput } from "@mantine/core";
import { Form, useForm } from "@mantine/form";
import { useEditor } from "@tiptap/react";
import { redirect } from "next/navigation";
import { useState } from "react";


export default function NewPostPage() {
  console.log(">>> rendering NewPostPage");
  const title: string = "";
  const content: string = "";

  const form = useForm({
    mode: "uncontrolled",
    initialValues: {
      content: content,
      title: title,
    },
    validate:{
      title: (value) =>
        value.trim() === ""? "Title is required" : null,
      content: (value) =>
        value.trim() === ""? "Content is required" : null,
    }
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
        redirect("/posts");
      } else {
        console.error(data.error);
      }
    } catch (error) {
      console.error(error);
    }
  };

  const [errorLabel, setErrorLabel] = useState(null);

  const handleError = (validationErrors, values, event) => {
    console.log(validationErrors, values, event);
    if (validationErrors.title !== null) {
      setErrorLabel(validationErrors.title);
    }
    if (validationErrors.content !== null) {
      setErrorLabel(validationErrors.content);
    }
  };

  return (
    <AuthGuard>
      
      <form onSubmit={form.onSubmit(handleSubmit, handleError)}>
        <SimpleGrid cols={1} spacing={2} verticalSpacing={"lg"}>
        <TextInput
          withAsterisk
          label="Title:"
          placeholder=""
          key={form.key("title")}
          {...form.getInputProps("title")}
        />

   
        <PostEdit content={content} key={form.key("content")} 
          {...form.getInputProps("content")}></PostEdit>
          {(errorLabel !== null) && (
          <Text c={"red"} size="xs">
            {errorLabel}
          </Text>
        )}

        <Group justify="flex-end" mt="md">
          <Button type="submit">Submit</Button>
        </Group>
          
          </SimpleGrid>
      </form>
    </AuthGuard>
  );
}
