import type { Meta, StoryObj } from "@storybook/nextjs-vite";

import { expect, within } from "storybook/test";

import { PostData } from "../../lib/definitions";
import Post from "./Post";

const samplePost: PostData = {
  id: "1",
  title: "Hello World",
  titleUrl: "hello-world",
  content: "<p>This is <strong>post</strong> content</p>",
  author: "Alice",
  createdOn: "2026-03-08",
  modifiedOn: "2026-03-08",
};

const meta = {
  title: "Components/Post",
  component: Post,
  tags: ["autodocs"],
  args: {
    post: samplePost,
    allowHtmlMarkup: true,
  },
} satisfies Meta<typeof Post>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {};

// interaction test ensures basic fields render correctly
Default.play = async ({ canvasElement }) => {
  const canvas = within(canvasElement);
  // title should be visible
  await expect(
    canvas.getByRole("heading", { name: /hello world/i }),
  ).toBeInTheDocument();
  // content from the editor should render
  await expect(canvas.getByText(/this is post content/i)).toBeInTheDocument();
  // author and date appear
  await expect(canvas.getByText(/alice/i)).toBeInTheDocument();
  await expect(canvas.getByText(/2026-03-08/)).toBeInTheDocument();
};
