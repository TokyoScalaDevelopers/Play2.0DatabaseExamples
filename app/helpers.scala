import models._

import play.api.data.Field

package object helpers {
  def threadIdentifier2URL(threadid: ThreadIdentifier) = {
    val ThreadIdentifier(date, shortTitle, random) = threadid
    controllers.routes.Application.threadView(date.getTime(), shortTitle, random)
  }

  def thread2URL(thread: Thread) = {
    threadIdentifier2URL(thread.threadID)
  }

  def inputHidden(field: Field) = {
    <input type="hidden" name={field.name} value={field.value.getOrElse("")} />
  }
}
