import {
  fetchPostById,
  fetchPostByTitle,
  updatePost,
} from "@/app/lib/postsApiCall";
import { error } from "console";
import { NextRequest, NextResponse } from "next/server";
import { z } from "zod";

export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ param: string }> }
) {
  const { param } = await params;

  console.info(">>> GET post with param: ", param);

  let response;
  if (isNumeric(param)) {
    // fetch by numeric ID
    response = await fetchPostById(param);
  } else {
    // fetch by slug
    response = await fetchPostByTitle(param);
  }

  if (response) {
    return NextResponse.json(response);
  } else {
    return NextResponse.json(
      { success: false, message: response },
      { status: 400 }
    );
  }
}

export async function PATCH(request: NextRequest) {
  console.info(">>> PATCH post with param: ");

  const body = await request.json();
  console.log("Received from client:", body);

  const NewPostSchema = z.object({
    title: z
      .string()
      .min(3, { message: "Title must be at least 3 characters long" })
      .trim(),
  });

  const validatedField = NewPostSchema.safeParse(body);

  if (!validatedField.success) {
    const issues = validatedField.error.errors;
    const messages = issues.map((issue) => issue.message);

    return NextResponse.json(
      { success: false, message: messages },
      { status: 403 }
    );
  }

  const response = await updatePost(body);

  if (response) {
    return NextResponse.json(response);
  } else {
    return NextResponse.json(
      { success: false, message: response },
      { status: 400 }
    );
  }
}

function isNumeric(arg: string): boolean {
  if (!arg) throw error("arg is null|undefined");

  return /^\d+$/.test(arg);
}
