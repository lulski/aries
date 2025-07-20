export interface Post {
  id: string;
  title: string;
  content: string;
  createdOn: string;
  modifiedOn: string;
  author: string;
}

export interface PostApiResponse {
  postDto: Post[];
  message: string;
  page: number;
  size: number;
  total: number;
}

const API_POST_URL = process.env.API_POST_URL;
const API_USERNAME = process.env.API_USERNAME;
const API_PASSWORD = process.env.API_PASSWORD;

export async function fetchPost(
  fetchPostUrl: string
): Promise<PostApiResponse> {
  const USERNAME = process.env.API_USERNAME;
  const PASSWORD = process.env.API_PASSWORD;

  try {
    const response = await fetch(fetchPostUrl, {
      headers: {
        Authorization:
          "Basic " + Buffer.from(`${USERNAME}:${PASSWORD}`).toString("base64"),
      },
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    return data as PostApiResponse;
  } catch (error) {
    console.error("Error fetching post:", error);
    throw new Error("Failed to fetch post");
  }
}

export async function fetchPostById(id: string) {
  const fetchPostUrl = API_POST_URL + "?id=" + id;
  return fetchPost(fetchPostUrl);
}

export async function fetchPostByTitle(title: string) {
  const fetchPostUrl = API_POST_URL + "?title=" + title;
  return fetchPost(fetchPostUrl);
}

export async function fetchPaginatedPosts(
  currentPage: number,
  size: number
): Promise<PostApiResponse> {
  const fetchUrl = `${API_POST_URL}?page=${currentPage}&size=${size}`;

  const response = await fetch(fetchUrl, {
    headers: {
      Authorization:
        "Basic " +
        Buffer.from(`${API_USERNAME}:${API_PASSWORD}`).toString("base64"),
    },
  });

  if (!response.ok) {
    throw new Error("Failed to fetch posts");
  }

  return response.json();
}

export async function savePost(body: string): Promise<PostApiResponse> {
  const fetchUrl = `${API_POST_URL}`;

  const response = await fetch(fetchUrl, {
    method: "POST",
    headers: {
      Authorization:
        "Basic " +
        Buffer.from(`${API_USERNAME}:${API_PASSWORD}`).toString("base64"),
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });

  if (!response.ok) {
    throw new Error("Failed to save posts");
  }

  return response.json();
}
