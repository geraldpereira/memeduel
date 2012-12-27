package fr.byob.game.memeduel.core.net;

import playn.core.util.Callback;

/**
 * A buffer for retrieving a list of objects
 * 
 * @author Kojiro
 */
public abstract class ListQueryBuffer<T> {

	private static class BufferItem<T> {
		private BufferItem<T> previous;
		private BufferItem<T> next;
		private T value;
		private int index;

		public boolean hasNext() {
			return next != null;
		}

		public boolean hasPrevious() {
			return previous != null;
		}
	}

	private BufferItem<T> first;
	private BufferItem<T> last;
	private BufferItem<T> current;

	private final int querySize;
	private final int maxBufferSize;
	private int bufferSize;

	public ListQueryBuffer(final int querySize) {
		this.querySize = querySize;
		this.maxBufferSize = 2 * querySize;
		this.bufferSize = 0;
	}

	public boolean hasNext() {
		return current.hasNext();
	}

	public boolean hasPrevious() {
		return current.hasPrevious();
	}

	public void init(final Callback<T> callBack) {
		queryList(querySize, 0, new Callback<T[]>() {
			@Override
			public void onSuccess(final T[] result) {
				if (result == null || result.length == 0) {
					callBack.onFailure(new Throwable("No result found"));
				} else {
					for (int i = 0; i < result.length; i++) {
						final T value = result[i];
						final BufferItem<T> item = new BufferItem<T>();
						item.index = i;
						item.value = value;

						if (current != null) {
							item.previous = current;
							current.next = item;
						}
						current = item;

						if (i == 0) {
							first = item;
						}

						if (i == result.length - 1) {
							last = item;
						}

						bufferSize++;
					}
					current = first;
					callBack.onSuccess(current.value);
				}

			}

			@Override
			public void onFailure(final Throwable cause) {
				if (current != null) {
					callBack.onSuccess(current.value);
				} else {
					callBack.onFailure(cause);
				}

			}
		});
	}

	public void getNextItem(final Callback<T> callBack) {
		if (current == null) {
			init(callBack);
			return;
		}

		current = current.next;
		if (!current.hasNext()) {
			// Fetch datas
			queryList(querySize, current.index + 1, new Callback<T[]>() {
				@Override
				public void onSuccess(final T[] result) {
					if (result != null) {
						for (final T value : result) {
							final BufferItem<T> item = new BufferItem<T>();
							item.value = value;
							item.index = last.index + 1;
							item.previous = last;
							last.next = item;
							last = item;

							bufferSize++;

							// Check that the buffer is not full
							if (bufferSize > maxBufferSize) {
								first = first.next;
								bufferSize--;
							}
						}
					}
					callBack.onSuccess(current.value);
				}

				@Override
				public void onFailure(final Throwable cause) {
					callBack.onSuccess(current.value);
				}
			});
		} else {
			callBack.onSuccess(current.value);
		}
	}

	public void getPreviousItem(final Callback<T> callBack) {
		current = current.previous;
		if (!current.hasPrevious() && current.index > 0) {
			// Fetch datas
			queryList(querySize, Math.max(0, current.index - querySize), new Callback<T[]>() {
				@Override
				public void onSuccess(final T[] result) {
					if (result != null) {
						for (int i = result.length - 1; i >= 0; i--) {
							final T value = result[i];
							final BufferItem<T> item = new BufferItem<T>();
							item.value = value;
							item.index = first.index - 1;
							item.next = first;
							first.previous = item;
							first = item;

							bufferSize++;

							// Check that the buffer is not full
							if (bufferSize > maxBufferSize) {
								last = last.previous;
								bufferSize--;
							}
						}

					}
					callBack.onSuccess(current.value);
				}

				@Override
				public void onFailure(final Throwable cause) {
					callBack.onSuccess(current.value);
				}
			});
		} else {
			callBack.onSuccess(current.value);
		}
	}

	public T getCurrentItem() {
		return current.value;
	}

	public void deleteCurrentItem(final Callback<T> callBack) {
		queryDelete(current.value, new Callback<String>() {
			@Override
			public void onSuccess(final String result) {
				if (current.previous != null) {
					current.previous.next = current.next;
				}

				if (current.next != null) {
					current.next.previous = current.previous;
				}

				if (hasNext()) {
					getNextItem(callBack);
				} else if (hasPrevious()) {
					getPreviousItem(callBack);
				} else {
					callBack.onFailure(new Throwable("No more result"));
				}
			}

			@Override
			public void onFailure(final Throwable cause) {
				callBack.onFailure(cause);
			}
		});
	}

	protected abstract void queryList(final int limit, final int offset, final Callback<T[]> callBack);

	protected abstract void queryDelete(final T value, final Callback<String> callBack);

}
