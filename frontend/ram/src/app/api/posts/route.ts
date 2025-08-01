import { fetchPaginatedPosts, savePost } from "@/app/lib/postsApiCall";
import { NextResponse } from "next/server";
import { z } from "zod";

const API_URL = process.env.API_POST_URL;
const USERNAME = process.env.API_USERNAME;
const PASSWORD = process.env.API_PASSWORD;

export async function GET(request: Request) {
  const { searchParams } = new URL(request.url);

  const page = searchParams.get("page")
    ? parseInt(searchParams.get("page") ?? "0")
    : 0;
  const size = searchParams.get("size")
    ? parseInt(searchParams.get("size") ?? "10")
    : 10;

  const data = await fetchPaginatedPosts(page, size);

  console.log("Fetched posts:", data);

  return NextResponse.json(data);
}

export async function POST(request: Request) {
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

  const response = await savePost(body);

  const data = response;

  return NextResponse.json({ success: true, data });
}
