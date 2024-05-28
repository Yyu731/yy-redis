import dayjs from 'dayjs'
import utc from 'dayjs/plugin/utc'

dayjs.extend(utc)

export function formatUTC(
    utcString,
    format = 'YYYY-MM-DD'
) {
    const resultTime = dayjs.utc(utcString).utcOffset(0).format(format)
    return resultTime
}
