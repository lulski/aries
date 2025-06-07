export async function fetchPost(fetchPostUrl: string) {
  const USERNAME = process.env.API_USERNAME;
  const PASSWORD = process.env.API_PASSWORD;
  //const fetchPostUrl = API_POST_URL + "?id=" + id;

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
    return { success: true, data: data };
  } catch (error) {
    console.error("Error fetching post:", error);
    return {
      success: false,
      error:
        error instanceof Error ? error.message : "An unknown error occurred",
    };
  }
}

export async function fetchPostById(id: string) {
  const API_POST_URL = process.env.API_POST_URL;
  const fetchPostUrl = API_POST_URL + "?id=" + id;
  fetchPost(fetchPostUrl);
}

export async function fetchPostByTitle(title: string) {
  const API_POST_URL = process.env.API_POST_URL;
  const fetchPostUrl = API_POST_URL + "?title=" + title;
  return fetchPost(fetchPostUrl);
}
