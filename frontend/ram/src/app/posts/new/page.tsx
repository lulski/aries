"use client"

import AuthGuard from "@/app/components/AuthGuard";
import PostEdit from "@/app/components/PostEdit";
import { SessionProvider, useSession } from "@/app/context/SessionContext";
import { SessionData } from "@/app/lib/definitions";
//import { getSessionData } from "@/app/lib/sessionUtil";
import { useForm } from "@mantine/form";
import { redirect } from "next/navigation";
import { string } from "zod";

export default function NewPostPage() {
  console.log(">>> fetching cookie");
  const title = "new title";
  const content:string = "new content";

  const form = useForm({
    mode: "uncontrolled",
    initialValues: {
      content: content,
      title: title,
    }
  })

  return (
    <AuthGuard>
      <h1>Create a New Post </h1>
      <PostEdit title={title} content={content}></PostEdit>
    </AuthGuard>
  );

}
